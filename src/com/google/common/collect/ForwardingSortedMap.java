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
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedMap
import|;
end_import

begin_comment
comment|/**  * A sorted map which forwards all its method calls to another sorted map.  * Subclasses should override one or more methods to modify the behavior of  * the backing sorted map as desired per the<a  * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  * @see ForwardingObject  * @author Mike Bostock  * @since 2 (imported from Google Collections Library)  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ForwardingSortedMap
specifier|public
specifier|abstract
class|class
name|ForwardingSortedMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ForwardingMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|implements
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|method|delegate ()
annotation|@
name|Override
specifier|protected
specifier|abstract
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|()
function_decl|;
DECL|method|comparator ()
specifier|public
name|Comparator
argument_list|<
name|?
super|super
name|K
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
DECL|method|firstKey ()
specifier|public
name|K
name|firstKey
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|firstKey
argument_list|()
return|;
block|}
DECL|method|headMap (K toKey)
specifier|public
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|headMap
parameter_list|(
name|K
name|toKey
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|headMap
argument_list|(
name|toKey
argument_list|)
return|;
block|}
DECL|method|lastKey ()
specifier|public
name|K
name|lastKey
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|lastKey
argument_list|()
return|;
block|}
DECL|method|subMap (K fromKey, K toKey)
specifier|public
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|subMap
parameter_list|(
name|K
name|fromKey
parameter_list|,
name|K
name|toKey
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|subMap
argument_list|(
name|fromKey
argument_list|,
name|toKey
argument_list|)
return|;
block|}
DECL|method|tailMap (K fromKey)
specifier|public
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|tailMap
parameter_list|(
name|K
name|fromKey
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|tailMap
argument_list|(
name|fromKey
argument_list|)
return|;
block|}
block|}
end_class

end_unit

