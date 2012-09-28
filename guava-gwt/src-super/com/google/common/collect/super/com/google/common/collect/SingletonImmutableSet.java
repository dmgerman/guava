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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
name|collect
operator|.
name|Iterators
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
name|collect
operator|.
name|UnmodifiableIterator
import|;
end_import

begin_comment
comment|/**  * GWT emulation of {@link SingletonImmutableSet}.  *  * @author Hayward Chan  */
end_comment

begin_class
DECL|class|SingletonImmutableSet
specifier|final
class|class
name|SingletonImmutableSet
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableSet
argument_list|<
name|E
argument_list|>
block|{
comment|// This reference is used both by the custom field serializer, and by the
comment|// GWT compiler to infer the elements of the lists that needs to be
comment|// serialized.
comment|//
comment|// Although this reference is non-final, it doesn't change after set creation.
DECL|field|element
name|E
name|element
decl_stmt|;
DECL|method|SingletonImmutableSet (E element)
name|SingletonImmutableSet
parameter_list|(
name|E
name|element
parameter_list|)
block|{
name|this
operator|.
name|element
operator|=
name|checkNotNull
argument_list|(
name|element
argument_list|)
expr_stmt|;
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
literal|1
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
name|singletonIterator
argument_list|(
name|element
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
name|element
operator|.
name|equals
argument_list|(
name|object
argument_list|)
return|;
block|}
block|}
end_class

end_unit

