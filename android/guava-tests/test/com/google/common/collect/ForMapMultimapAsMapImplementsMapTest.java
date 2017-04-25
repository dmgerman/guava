begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Test {@link Multimap#asMap()} for a {@link Multimaps#forMap} multimap with  * {@link MapInterfaceTest}.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ForMapMultimapAsMapImplementsMapTest
specifier|public
class|class
name|ForMapMultimapAsMapImplementsMapTest
extends|extends
name|AbstractMultimapAsMapImplementsMapTest
block|{
DECL|method|ForMapMultimapAsMapImplementsMapTest ()
specifier|public
name|ForMapMultimapAsMapImplementsMapTest
parameter_list|()
block|{
name|super
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
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
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
return|return
name|Multimaps
operator|.
name|forMap
argument_list|(
name|map
argument_list|)
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
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"cow"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
return|return
name|Multimaps
operator|.
name|forMap
argument_list|(
name|map
argument_list|)
operator|.
name|asMap
argument_list|()
return|;
block|}
DECL|method|testEntrySetRemoveAllNullFromEmpty ()
annotation|@
name|Override
specifier|public
name|void
name|testEntrySetRemoveAllNullFromEmpty
parameter_list|()
block|{
try|try
block|{
name|super
operator|.
name|testEntrySetRemoveAllNullFromEmpty
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|tolerated
parameter_list|)
block|{
comment|// GWT's HashMap.entrySet().removeAll(null) doesn't throws NPE.
block|}
block|}
DECL|method|testEntrySetRetainAllNullFromEmpty ()
annotation|@
name|Override
specifier|public
name|void
name|testEntrySetRetainAllNullFromEmpty
parameter_list|()
block|{
try|try
block|{
name|super
operator|.
name|testEntrySetRetainAllNullFromEmpty
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|tolerated
parameter_list|)
block|{
comment|// GWT's HashMap.entrySet().retainAll(null) doesn't throws NPE.
block|}
block|}
DECL|method|testKeySetRemoveAllNullFromEmpty ()
annotation|@
name|Override
specifier|public
name|void
name|testKeySetRemoveAllNullFromEmpty
parameter_list|()
block|{
try|try
block|{
name|super
operator|.
name|testKeySetRemoveAllNullFromEmpty
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|tolerated
parameter_list|)
block|{
comment|// GWT's HashMap.keySet().removeAll(null) doesn't throws NPE.
block|}
block|}
DECL|method|testKeySetRetainAllNullFromEmpty ()
annotation|@
name|Override
specifier|public
name|void
name|testKeySetRetainAllNullFromEmpty
parameter_list|()
block|{
try|try
block|{
name|super
operator|.
name|testKeySetRetainAllNullFromEmpty
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|tolerated
parameter_list|)
block|{
comment|// GWT's HashMap.keySet().retainAll(null) doesn't throws NPE.
block|}
block|}
DECL|method|testValuesRemoveAllNullFromEmpty ()
annotation|@
name|Override
specifier|public
name|void
name|testValuesRemoveAllNullFromEmpty
parameter_list|()
block|{
try|try
block|{
name|super
operator|.
name|testValuesRemoveAllNullFromEmpty
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|tolerated
parameter_list|)
block|{
comment|// GWT's HashMap.values().removeAll(null) doesn't throws NPE.
block|}
block|}
DECL|method|testValuesRetainAllNullFromEmpty ()
annotation|@
name|Override
specifier|public
name|void
name|testValuesRetainAllNullFromEmpty
parameter_list|()
block|{
try|try
block|{
name|super
operator|.
name|testValuesRemoveAllNullFromEmpty
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|tolerated
parameter_list|)
block|{
comment|// GWT's HashMap.values().retainAll(null) doesn't throws NPE.
block|}
block|}
block|}
end_class

end_unit
