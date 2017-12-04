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
import|import static
name|java
operator|.
name|util
operator|.
name|Collections
operator|.
name|singletonList
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

begin_comment
comment|/**  * GWT emulated version of {@link SingletonImmutableList}.  *  * @author Hayward Chan  */
end_comment

begin_class
DECL|class|SingletonImmutableList
specifier|final
class|class
name|SingletonImmutableList
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ForwardingImmutableList
argument_list|<
name|E
argument_list|>
block|{
DECL|field|delegate
specifier|final
specifier|transient
name|List
argument_list|<
name|E
argument_list|>
name|delegate
decl_stmt|;
comment|// This reference is used both by the custom field serializer, and by the
comment|// GWT compiler to infer the elements of the lists that needs to be
comment|// serialized.
DECL|field|element
name|E
name|element
decl_stmt|;
DECL|method|SingletonImmutableList (E element)
name|SingletonImmutableList
parameter_list|(
name|E
name|element
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|singletonList
argument_list|(
name|checkNotNull
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|element
operator|=
name|element
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|delegateList ()
name|List
argument_list|<
name|E
argument_list|>
name|delegateList
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
block|}
end_class

end_unit

