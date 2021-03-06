begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing.google
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
operator|.
name|google
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Multimap
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
name|testing
operator|.
name|SampleElements
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
name|testing
operator|.
name|TestContainerGenerator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
import|;
end_import

begin_comment
comment|/**  * Creates multimaps, containing sample elements, to be tested.  *  * @author Louis Wasserman  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
DECL|interface|TestMultimapGenerator
specifier|public
interface|interface
name|TestMultimapGenerator
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|,
name|M
extends|extends
name|Multimap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
parameter_list|>
extends|extends
name|TestContainerGenerator
argument_list|<
name|M
argument_list|,
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
block|{
DECL|method|createKeyArray (int length)
name|K
index|[]
name|createKeyArray
parameter_list|(
name|int
name|length
parameter_list|)
function_decl|;
DECL|method|createValueArray (int length)
name|V
index|[]
name|createValueArray
parameter_list|(
name|int
name|length
parameter_list|)
function_decl|;
DECL|method|sampleKeys ()
name|SampleElements
argument_list|<
name|K
argument_list|>
name|sampleKeys
parameter_list|()
function_decl|;
DECL|method|sampleValues ()
name|SampleElements
argument_list|<
name|V
argument_list|>
name|sampleValues
parameter_list|()
function_decl|;
DECL|method|createCollection (Iterable<? extends V> values)
name|Collection
argument_list|<
name|V
argument_list|>
name|createCollection
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|values
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

