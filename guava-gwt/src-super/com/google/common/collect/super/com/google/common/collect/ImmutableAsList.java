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

begin_comment
comment|/**  * List returned by {@link ImmutableCollection#asList} that delegates {@code contains} checks  * to the backing collection.  *  * @author Jared Levy  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|,
name|emulated
operator|=
literal|true
argument_list|)
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
DECL|class|ImmutableAsList
specifier|abstract
class|class
name|ImmutableAsList
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableList
argument_list|<
name|E
argument_list|>
block|{
DECL|method|delegateCollection ()
specifier|abstract
name|ImmutableCollection
argument_list|<
name|E
argument_list|>
name|delegateCollection
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|contains (Object target)
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|target
parameter_list|)
block|{
comment|// The collection's contains() is at least as fast as ImmutableList's
comment|// and is often faster.
return|return
name|delegateCollection
argument_list|()
operator|.
name|contains
argument_list|(
name|target
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|delegateCollection
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
name|delegateCollection
argument_list|()
operator|.
name|isEmpty
argument_list|()
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
name|delegateCollection
argument_list|()
operator|.
name|isPartialView
argument_list|()
return|;
block|}
block|}
end_class

end_unit

