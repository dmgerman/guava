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
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_comment
comment|/**  * GWT emulation of {@link EmptyImmutableMap}.  In GWT, it is a thin wrapper  * around {@link java.util.Collections#emptyMap()}.  *  * @author Hayward Chan  */
end_comment

begin_class
DECL|class|EmptyImmutableMap
specifier|final
class|class
name|EmptyImmutableMap
extends|extends
name|ForwardingImmutableMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
block|{
DECL|method|EmptyImmutableMap ()
name|EmptyImmutableMap
parameter_list|()
block|{
name|super
argument_list|(
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|field|INSTANCE
specifier|static
specifier|final
name|EmptyImmutableMap
name|INSTANCE
init|=
operator|new
name|EmptyImmutableMap
argument_list|()
decl_stmt|;
block|}
end_class

end_unit

