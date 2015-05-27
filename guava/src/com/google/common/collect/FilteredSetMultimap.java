begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * A supertype for filtered {@link SetMultimap} implementations.  *  * @author Louis Wasserman  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
DECL|interface|FilteredSetMultimap
interface|interface
name|FilteredSetMultimap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|FilteredMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
extends|,
name|SetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
annotation|@
name|Override
DECL|method|unfiltered ()
name|SetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|unfiltered
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

