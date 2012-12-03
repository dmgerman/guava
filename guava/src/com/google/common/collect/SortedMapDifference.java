begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|SortedMap
import|;
end_import

begin_comment
comment|/**  * An object representing the differences between two sorted maps.  *  * @author Louis Wasserman  * @since 8.0  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
DECL|interface|SortedMapDifference
specifier|public
interface|interface
name|SortedMapDifference
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|MapDifference
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
annotation|@
name|Override
DECL|method|entriesOnlyOnLeft ()
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entriesOnlyOnLeft
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|entriesOnlyOnRight ()
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entriesOnlyOnRight
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|entriesInCommon ()
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entriesInCommon
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|entriesDiffering ()
name|SortedMap
argument_list|<
name|K
argument_list|,
name|ValueDifference
argument_list|<
name|V
argument_list|>
argument_list|>
name|entriesDiffering
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

