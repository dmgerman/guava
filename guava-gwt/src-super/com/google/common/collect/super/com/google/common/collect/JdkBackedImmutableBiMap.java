begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2018 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * GWT emulation of {@link JdkBackedImmutableBiMap}. Never used, but must exist so that the client  * is willing to deserialize maps that were this type on the server.  */
end_comment

begin_class
DECL|class|JdkBackedImmutableBiMap
class|class
name|JdkBackedImmutableBiMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|method|JdkBackedImmutableBiMap (ImmutableMap<K, V> delegate, ImmutableBiMap<V, K> inverse)
specifier|private
name|JdkBackedImmutableBiMap
parameter_list|(
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|,
name|ImmutableBiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inverse
parameter_list|)
block|{
name|super
argument_list|(
name|delegate
argument_list|,
name|inverse
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

