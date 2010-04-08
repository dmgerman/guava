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
comment|/**  * A multiset which forwards all its method calls to another multiset.  * Subclasses should override one or more methods to modify the behavior of the  * backing multiset as desired per the<a  * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  * @see ForwardingObject  * @author Kevin Bourrillion  * @since 2 (imported from Google Collections Library)  */
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
DECL|method|delegate ()
annotation|@
name|Override
specifier|protected
specifier|abstract
name|Multiset
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|()
function_decl|;
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
block|}
end_class

end_unit

