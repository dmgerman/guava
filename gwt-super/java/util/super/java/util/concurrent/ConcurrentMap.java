begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Minimal GWT emulation of a map providing atomic operations.  *  * @author jessewilson@google.com (Jesse Wilson)  */
end_comment

begin_interface
DECL|interface|ConcurrentMap
specifier|public
interface|interface
name|ConcurrentMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|method|putIfAbsent (K key, V value)
name|V
name|putIfAbsent
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
function_decl|;
DECL|method|remove (Object key, Object value)
name|boolean
name|remove
parameter_list|(
name|Object
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
function_decl|;
DECL|method|replace (K key, V value)
name|V
name|replace
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
function_decl|;
DECL|method|replace (K key, V oldValue, V newValue)
name|boolean
name|replace
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|oldValue
parameter_list|,
name|V
name|newValue
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

