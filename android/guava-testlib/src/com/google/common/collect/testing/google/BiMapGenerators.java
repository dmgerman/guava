begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|BiMap
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
name|ImmutableBiMap
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
name|Maps
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
comment|/**  * Generators of various {@link com.google.common.collect.BiMap}s and derived collections.  *  * @author Jared Levy  * @author Hayward Chan  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|BiMapGenerators
specifier|public
class|class
name|BiMapGenerators
block|{
DECL|class|ImmutableBiMapGenerator
specifier|public
specifier|static
class|class
name|ImmutableBiMapGenerator
extends|extends
name|TestStringBiMapGenerator
block|{
annotation|@
name|Override
DECL|method|create (Entry<String, String>[] entries)
specifier|protected
name|BiMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|create
parameter_list|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
index|[]
name|entries
parameter_list|)
block|{
name|ImmutableBiMap
operator|.
name|Builder
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|builder
init|=
name|ImmutableBiMap
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|builder
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
block|}
DECL|class|ImmutableBiMapCopyOfGenerator
specifier|public
specifier|static
class|class
name|ImmutableBiMapCopyOfGenerator
extends|extends
name|TestStringBiMapGenerator
block|{
annotation|@
name|Override
DECL|method|create (Entry<String, String>[] entries)
specifier|protected
name|BiMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|create
parameter_list|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
index|[]
name|entries
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|builder
init|=
name|Maps
operator|.
name|newLinkedHashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|builder
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|ImmutableBiMap
operator|.
name|copyOf
argument_list|(
name|builder
argument_list|)
return|;
block|}
block|}
DECL|class|ImmutableBiMapCopyOfEntriesGenerator
specifier|public
specifier|static
class|class
name|ImmutableBiMapCopyOfEntriesGenerator
extends|extends
name|TestStringBiMapGenerator
block|{
annotation|@
name|Override
DECL|method|create (Entry<String, String>[] entries)
specifier|protected
name|BiMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|create
parameter_list|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
index|[]
name|entries
parameter_list|)
block|{
return|return
name|ImmutableBiMap
operator|.
name|copyOf
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|entries
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

