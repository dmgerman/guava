begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|SortedMapInterfaceTest
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_comment
comment|/**  * Tests for {@link ForwardingSortedMap} using {@link MapInterfaceTest}.  *  * @author George van den Driessche  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ForwardingSortedMapImplementsMapTest
specifier|public
class|class
name|ForwardingSortedMapImplementsMapTest
extends|extends
name|SortedMapInterfaceTest
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
block|{
DECL|class|SimpleForwardingSortedMap
specifier|private
specifier|static
class|class
name|SimpleForwardingSortedMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ForwardingSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|delegate
specifier|final
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
decl_stmt|;
DECL|method|SimpleForwardingSortedMap (SortedMap<K, V> delegate)
name|SimpleForwardingSortedMap
parameter_list|(
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
DECL|method|delegate ()
annotation|@
name|Override
specifier|protected
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
block|}
DECL|method|ForwardingSortedMapImplementsMapTest ()
specifier|public
name|ForwardingSortedMapImplementsMapTest
parameter_list|()
block|{
name|super
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|,
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
name|SortedMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|makeEmptyMap
parameter_list|()
block|{
return|return
operator|new
name|SimpleForwardingSortedMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|(
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|nullsFirst
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|makePopulatedMap ()
annotation|@
name|Override
specifier|protected
name|SortedMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|makePopulatedMap
parameter_list|()
block|{
specifier|final
name|SortedMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|sortedMap
init|=
name|makeEmptyMap
argument_list|()
decl_stmt|;
name|sortedMap
operator|.
name|put
argument_list|(
literal|"one"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|sortedMap
operator|.
name|put
argument_list|(
literal|"two"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|sortedMap
operator|.
name|put
argument_list|(
literal|"three"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
return|return
name|sortedMap
return|;
block|}
DECL|method|getKeyNotInPopulatedMap ()
annotation|@
name|Override
specifier|protected
name|String
name|getKeyNotInPopulatedMap
parameter_list|()
throws|throws
name|UnsupportedOperationException
block|{
return|return
literal|"minus one"
return|;
block|}
DECL|method|getValueNotInPopulatedMap ()
annotation|@
name|Override
specifier|protected
name|Integer
name|getValueNotInPopulatedMap
parameter_list|()
throws|throws
name|UnsupportedOperationException
block|{
return|return
operator|-
literal|1
return|;
block|}
DECL|method|testContainsKey ()
annotation|@
name|Override
specifier|public
name|void
name|testContainsKey
parameter_list|()
block|{
try|try
block|{
name|super
operator|.
name|testContainsKey
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|tolerated
parameter_list|)
block|{     }
block|}
DECL|method|testEntrySetContainsEntryIncompatibleKey ()
annotation|@
name|Override
specifier|public
name|void
name|testEntrySetContainsEntryIncompatibleKey
parameter_list|()
block|{
try|try
block|{
name|super
operator|.
name|testEntrySetContainsEntryIncompatibleKey
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|tolerated
parameter_list|)
block|{     }
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
comment|// GWT's TreeMap.entrySet().removeAll(null) doesn't throws NPE.
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
comment|// GWT's TreeMap.entrySet().retainAll(null) doesn't throws NPE.
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
comment|// GWT's TreeMap.keySet().removeAll(null) doesn't throws NPE.
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
comment|// GWT's TreeMap.keySet().retainAll(null) doesn't throws NPE.
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
comment|// GWT's TreeMap.values().removeAll(null) doesn't throws NPE.
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
comment|// GWT's TreeMap.values().retainAll(null) doesn't throws NPE.
block|}
block|}
block|}
end_class

end_unit
