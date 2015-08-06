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
name|testing
operator|.
name|AbstractMapTester
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
name|Helpers
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|List
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
comment|/**  * Skeleton for a tester of a {@code BiMap}.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|SuppressUnderAndroid
DECL|class|AbstractBiMapTester
specifier|public
specifier|abstract
class|class
name|AbstractBiMapTester
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractMapTester
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
annotation|@
name|Override
DECL|method|getMap ()
specifier|protected
name|BiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getMap
parameter_list|()
block|{
return|return
operator|(
name|BiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|super
operator|.
name|getMap
argument_list|()
return|;
block|}
DECL|method|reverseEntry (Entry<K, V> entry)
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Entry
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|reverseEntry
parameter_list|(
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
parameter_list|)
block|{
return|return
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|expectContents (Collection<Entry<K, V>> expected)
specifier|protected
name|void
name|expectContents
parameter_list|(
name|Collection
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|expected
parameter_list|)
block|{
name|super
operator|.
name|expectContents
argument_list|(
name|expected
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Entry
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
argument_list|>
name|reversedEntries
init|=
operator|new
name|ArrayList
argument_list|<
name|Entry
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
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
name|expected
control|)
block|{
name|reversedEntries
operator|.
name|add
argument_list|(
name|reverseEntry
argument_list|(
name|entry
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Helpers
operator|.
name|assertEqualIgnoringOrder
argument_list|(
name|getMap
argument_list|()
operator|.
name|inverse
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|,
name|reversedEntries
argument_list|)
expr_stmt|;
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
name|expected
control|)
block|{
name|assertEquals
argument_list|(
literal|"Wrong key for value "
operator|+
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|getMap
argument_list|()
operator|.
name|inverse
argument_list|()
operator|.
name|get
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|expectMissing (Entry<K, V>.... entries)
specifier|protected
name|void
name|expectMissing
parameter_list|(
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
modifier|...
name|entries
parameter_list|)
block|{
name|super
operator|.
name|expectMissing
argument_list|(
name|entries
argument_list|)
expr_stmt|;
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
name|entries
control|)
block|{
name|Entry
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|reversed
init|=
name|reverseEntry
argument_list|(
name|entry
argument_list|)
decl_stmt|;
name|BiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inv
init|=
name|getMap
argument_list|()
operator|.
name|inverse
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Inverse should not contain entry "
operator|+
name|reversed
argument_list|,
name|inv
operator|.
name|entrySet
argument_list|()
operator|.
name|contains
argument_list|(
name|entry
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Inverse should not contain key "
operator|+
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|inv
operator|.
name|containsKey
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Inverse should not contain value "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|inv
operator|.
name|containsValue
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Inverse should not return a mapping for key "
operator|+
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|getMap
argument_list|()
operator|.
name|get
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

