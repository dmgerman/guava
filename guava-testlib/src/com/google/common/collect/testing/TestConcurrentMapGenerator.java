begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2015 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|testing
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
name|concurrent
operator|.
name|ConcurrentMap
import|;
end_import

begin_comment
comment|/**  * Creates concurrent maps, containing sample entries, to be tested.  *   * @author Louis Wasserman  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
DECL|interface|TestConcurrentMapGenerator
specifier|public
interface|interface
name|TestConcurrentMapGenerator
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|TestMapGenerator
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
annotation|@
name|Override
DECL|method|create (Object... elements)
name|ConcurrentMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Object
modifier|...
name|elements
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

