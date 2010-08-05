begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Collections
import|;
end_import

begin_comment
comment|/**  * GWT emulation of {@link SingletonImmutableMap}.  *  * @author Hayward Chan  */
end_comment

begin_class
DECL|class|SingletonImmutableMap
specifier|final
class|class
name|SingletonImmutableMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|// These references are used both by the custom field serializer, and by the
comment|// GWT compiler to infer the keys and values of the map that needs to be
comment|// serialized.
comment|//
comment|// Although they are non-final, they are package private and the reference is
comment|// never modified after a map is constructed.
DECL|field|singleKey
name|K
name|singleKey
decl_stmt|;
DECL|field|singleValue
name|V
name|singleValue
decl_stmt|;
DECL|method|SingletonImmutableMap (K key, V value)
name|SingletonImmutableMap
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
name|checkNotNull
argument_list|(
name|key
argument_list|)
argument_list|,
name|checkNotNull
argument_list|(
name|value
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|singleKey
operator|=
name|key
expr_stmt|;
name|this
operator|.
name|singleValue
operator|=
name|value
expr_stmt|;
block|}
block|}
end_class

end_unit

