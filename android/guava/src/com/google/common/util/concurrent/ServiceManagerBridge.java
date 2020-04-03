begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2020 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.util.concurrent
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
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
name|GwtIncompatible
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
name|ImmutableMultimap
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
name|util
operator|.
name|concurrent
operator|.
name|Service
operator|.
name|State
import|;
end_import

begin_comment
comment|/**  * Superinterface of {@link ServiceManager} to introduce a bridge method for {@code  * servicesByState()}, to ensure binary compatibility with older Guava versions that specified  * {@code servicesByState()} to return {@code ImmutableMultimap}.  */
end_comment

begin_interface
annotation|@
name|GwtIncompatible
DECL|interface|ServiceManagerBridge
interface|interface
name|ServiceManagerBridge
block|{
DECL|method|servicesByState ()
name|ImmutableMultimap
argument_list|<
name|State
argument_list|,
name|Service
argument_list|>
name|servicesByState
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

