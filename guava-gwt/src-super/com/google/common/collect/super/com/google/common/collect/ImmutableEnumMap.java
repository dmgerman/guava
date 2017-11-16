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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
comment|/**  * GWT emulation of {@link ImmutableEnumMap}. The type parameter is not bounded  * by {@code Enum<E>} to avoid code-size bloat.  *  * @author Hayward Chan  */
end_comment

begin_class
DECL|class|ImmutableEnumMap
specifier|final
class|class
name|ImmutableEnumMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ForwardingImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|method|asImmutable (Map<K, V> map)
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|asImmutable
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|)
block|{
for|for
control|(
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
range|:
name|checkNotNull
argument_list|(
name|map
argument_list|)
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|checkNotNull
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ImmutableEnumMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|map
argument_list|)
return|;
block|}
DECL|method|ImmutableEnumMap (Map<? extends K, ? extends V> delegate)
name|ImmutableEnumMap
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|delegate
parameter_list|)
block|{
name|super
argument_list|(
name|WellBehavedMap
operator|.
name|wrap
argument_list|(
name|delegate
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

