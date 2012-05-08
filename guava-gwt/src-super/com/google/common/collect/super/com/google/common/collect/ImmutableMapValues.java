begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Map
operator|.
name|Entry
import|;
end_import

begin_comment
comment|/**  * {@code values()} implementation for {@link ImmutableMap}.  *  * @author Jesse Wilson  * @author Kevin Bourrillion  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|ImmutableMapValues
specifier|abstract
class|class
name|ImmutableMapValues
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
block|{
DECL|method|ImmutableMapValues ()
name|ImmutableMapValues
parameter_list|()
block|{}
DECL|method|map ()
specifier|abstract
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|map
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|UnmodifiableIterator
argument_list|<
name|V
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|Maps
operator|.
name|valueIterator
argument_list|(
name|map
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
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
name|map
argument_list|()
operator|.
name|containsValue
argument_list|(
name|object
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isPartialView ()
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|createAsList ()
name|ImmutableList
argument_list|<
name|V
argument_list|>
name|createAsList
parameter_list|()
block|{
specifier|final
name|ImmutableList
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entryList
init|=
name|map
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|asList
argument_list|()
decl_stmt|;
return|return
operator|new
name|ImmutableAsList
argument_list|<
name|V
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|V
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|entryList
operator|.
name|get
argument_list|(
name|index
argument_list|)
operator|.
name|getValue
argument_list|()
return|;
block|}
annotation|@
name|Override
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|delegateCollection
parameter_list|()
block|{
return|return
name|ImmutableMapValues
operator|.
name|this
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

