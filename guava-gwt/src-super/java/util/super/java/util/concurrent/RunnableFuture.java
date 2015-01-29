begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2015 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|java.util.concurrent
package|package
name|java
operator|.
name|util
operator|.
name|concurrent
package|;
end_package

begin_comment
comment|/**  * Emulation of RunnableFuture.  */
end_comment

begin_interface
DECL|interface|RunnableFuture
specifier|public
interface|interface
name|RunnableFuture
parameter_list|<
name|V
parameter_list|>
extends|extends
name|Runnable
extends|,
name|Future
argument_list|<
name|V
argument_list|>
block|{ }
end_interface

end_unit

