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
name|java
operator|.
name|util
operator|.
name|Collections
operator|.
name|emptyList
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
comment|/**  * GWT emulated version of EmptyImmutableList.  *  * @author Hayward Chan  */
end_comment

begin_class
DECL|class|EmptyImmutableList
specifier|final
class|class
name|EmptyImmutableList
extends|extends
name|ForwardingImmutableList
argument_list|<
name|Object
argument_list|>
block|{
DECL|field|INSTANCE
specifier|static
specifier|final
name|EmptyImmutableList
name|INSTANCE
init|=
operator|new
name|EmptyImmutableList
argument_list|()
decl_stmt|;
DECL|method|EmptyImmutableList ()
specifier|private
name|EmptyImmutableList
parameter_list|()
block|{   }
DECL|method|delegateList ()
annotation|@
name|Override
name|List
argument_list|<
name|Object
argument_list|>
name|delegateList
parameter_list|()
block|{
return|return
name|emptyList
argument_list|()
return|;
block|}
block|}
end_class

end_unit

