begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.cache
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|cache
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
comment|/**  * Calculates the weights of cache entries.  *  * @author Charles Fry  * @since 11.0  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
annotation|@
name|FunctionalInterface
DECL|interface|Weigher
specifier|public
interface|interface
name|Weigher
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
block|{
comment|/**    * Returns the weight of a cache entry. There is no unit for entry weights; rather they are simply    * relative to each other.    *    * @return the weight of the entry; must be non-negative    */
DECL|method|weigh (K key, V value)
name|int
name|weigh
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

