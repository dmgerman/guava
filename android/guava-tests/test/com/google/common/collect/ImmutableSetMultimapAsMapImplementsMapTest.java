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
name|testing
operator|.
name|MapInterfaceTest
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
import|;
end_import

begin_comment
comment|/**  * Test {@link Multimap#asMap()} for an {@link ImmutableSetMultimap} with  * {@link MapInterfaceTest}.  *  * @author Mike Ward  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ImmutableSetMultimapAsMapImplementsMapTest
specifier|public
class|class
name|ImmutableSetMultimapAsMapImplementsMapTest
extends|extends
name|AbstractMultimapAsMapImplementsMapTest
block|{
DECL|method|ImmutableSetMultimapAsMapImplementsMapTest ()
specifier|public
name|ImmutableSetMultimapAsMapImplementsMapTest
parameter_list|()
block|{
name|super
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|makeEmptyMap ()
annotation|@
name|Override
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|makeEmptyMap
parameter_list|()
block|{
return|return
name|ImmutableSetMultimap
operator|.
expr|<
name|String
operator|,
name|Integer
operator|>
name|of
argument_list|()
operator|.
name|asMap
argument_list|()
return|;
block|}
DECL|method|makePopulatedMap ()
annotation|@
name|Override
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|makePopulatedMap
parameter_list|()
block|{
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|delegate
init|=
name|HashMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
name|populate
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
return|return
name|ImmutableSetMultimap
operator|.
name|copyOf
argument_list|(
name|delegate
argument_list|)
operator|.
name|asMap
argument_list|()
return|;
block|}
block|}
end_class

end_unit
