begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing.testers
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
name|testers
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
name|collect
operator|.
name|testing
operator|.
name|features
operator|.
name|CollectionFeature
operator|.
name|SUPPORTS_ITERATOR_REMOVE
import|;
end_import

begin_import
import|import static
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
name|features
operator|.
name|CollectionSize
operator|.
name|ONE
import|;
end_import

begin_import
import|import static
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
name|features
operator|.
name|CollectionSize
operator|.
name|ZERO
import|;
end_import

begin_import
import|import static
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
name|features
operator|.
name|MapFeature
operator|.
name|ALLOWS_NULL_KEYS
import|;
end_import

begin_import
import|import static
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
name|features
operator|.
name|MapFeature
operator|.
name|ALLOWS_NULL_KEY_QUERIES
import|;
end_import

begin_import
import|import static
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
name|features
operator|.
name|MapFeature
operator|.
name|ALLOWS_NULL_VALUES
import|;
end_import

begin_import
import|import static
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
name|features
operator|.
name|MapFeature
operator|.
name|ALLOWS_NULL_VALUE_QUERIES
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
name|annotations
operator|.
name|GwtIncompatible
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
name|features
operator|.
name|CollectionFeature
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
name|features
operator|.
name|CollectionSize
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
name|features
operator|.
name|MapFeature
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * Tests {@link java.util.Map#entrySet}.  *   * @author Louis Wasserman  * @param<K> The key type of the map implementation under test.  * @param<V> The value type of the map implementation under test.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|SuppressUnderAndroid
DECL|class|MapEntrySetTester
specifier|public
class|class
name|MapEntrySetTester
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
DECL|enum|IncomparableType
specifier|private
enum|enum
name|IncomparableType
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;   }
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ONE
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ITERATOR_REMOVE
argument_list|)
DECL|method|testEntrySetIteratorRemove ()
specifier|public
name|void
name|testEntrySetIteratorRemove
parameter_list|()
block|{
name|Set
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entrySet
init|=
name|getMap
argument_list|()
operator|.
name|entrySet
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entryItr
init|=
name|entrySet
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|e0
argument_list|()
argument_list|,
name|entryItr
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|entryItr
operator|.
name|remove
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|getMap
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|entrySet
operator|.
name|contains
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testContainsEntryWithIncomparableKey ()
specifier|public
name|void
name|testContainsEntryWithIncomparableKey
parameter_list|()
block|{
try|try
block|{
name|assertFalse
argument_list|(
name|getMap
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|contains
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|IncomparableType
operator|.
name|INSTANCE
argument_list|,
name|v0
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|acceptable
parameter_list|)
block|{
comment|// allowed by the spec
block|}
block|}
DECL|method|testContainsEntryWithIncomparableValue ()
specifier|public
name|void
name|testContainsEntryWithIncomparableValue
parameter_list|()
block|{
try|try
block|{
name|assertFalse
argument_list|(
name|getMap
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|contains
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|k0
argument_list|()
argument_list|,
name|IncomparableType
operator|.
name|INSTANCE
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|acceptable
parameter_list|)
block|{
comment|// allowed by the spec
block|}
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|ALLOWS_NULL_KEY_QUERIES
argument_list|)
DECL|method|testContainsEntryWithNullKeyAbsent ()
specifier|public
name|void
name|testContainsEntryWithNullKeyAbsent
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|getMap
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|contains
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
literal|null
argument_list|,
name|v0
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|ALLOWS_NULL_KEYS
argument_list|)
DECL|method|testContainsEntryWithNullKeyPresent ()
specifier|public
name|void
name|testContainsEntryWithNullKeyPresent
parameter_list|()
block|{
name|initMapWithNullKey
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|getMap
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|contains
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
literal|null
argument_list|,
name|getValueForNullKey
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|ALLOWS_NULL_VALUE_QUERIES
argument_list|)
DECL|method|testContainsEntryWithNullValueAbsent ()
specifier|public
name|void
name|testContainsEntryWithNullValueAbsent
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|getMap
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|contains
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|k0
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|ALLOWS_NULL_VALUES
argument_list|)
DECL|method|testContainsEntryWithNullValuePresent ()
specifier|public
name|void
name|testContainsEntryWithNullValuePresent
parameter_list|()
block|{
name|initMapWithNullValue
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|getMap
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|contains
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|getKeyForNullValue
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"reflection"
argument_list|)
DECL|method|getContainsEntryWithIncomparableKeyMethod ()
specifier|public
specifier|static
name|Method
name|getContainsEntryWithIncomparableKeyMethod
parameter_list|()
block|{
return|return
name|Helpers
operator|.
name|getMethod
argument_list|(
name|MapEntrySetTester
operator|.
name|class
argument_list|,
literal|"testContainsEntryWithIncomparableKey"
argument_list|)
return|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"reflection"
argument_list|)
DECL|method|getContainsEntryWithIncomparableValueMethod ()
specifier|public
specifier|static
name|Method
name|getContainsEntryWithIncomparableValueMethod
parameter_list|()
block|{
return|return
name|Helpers
operator|.
name|getMethod
argument_list|(
name|MapEntrySetTester
operator|.
name|class
argument_list|,
literal|"testContainsEntryWithIncomparableValue"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

