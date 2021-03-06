begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2018 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|GwtIncompatible
import|;
end_import

begin_import
import|import
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|qual
operator|.
name|Nullable
import|;
end_import

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|IndexedImmutableSet
specifier|abstract
class|class
name|IndexedImmutableSet
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableSet
argument_list|<
name|E
argument_list|>
block|{
DECL|method|get (int index)
specifier|abstract
name|E
name|get
parameter_list|(
name|int
name|index
parameter_list|)
function_decl|;
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
name|asList
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
block|}
annotation|@
name|Override
annotation|@
name|GwtIncompatible
DECL|method|copyIntoArray (@ullable Object[] dst, int offset)
name|int
name|copyIntoArray
parameter_list|(
annotation|@
name|Nullable
name|Object
index|[]
name|dst
parameter_list|,
name|int
name|offset
parameter_list|)
block|{
return|return
name|asList
argument_list|()
operator|.
name|copyIntoArray
argument_list|(
name|dst
argument_list|,
name|offset
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createAsList ()
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|createAsList
parameter_list|()
block|{
return|return
operator|new
name|ImmutableList
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|E
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|IndexedImmutableSet
operator|.
name|this
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
annotation|@
name|Override
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
name|IndexedImmutableSet
operator|.
name|this
operator|.
name|isPartialView
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|IndexedImmutableSet
operator|.
name|this
operator|.
name|size
argument_list|()
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

