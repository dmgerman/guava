begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|ALLOWS_NULL_VALUES
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
name|util
operator|.
name|LinkedHashMap
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
comment|/**  * A generic JUnit test which tests {@code toString()} operations on a map. Can't be invoked  * directly; please see {@link com.google.common.collect.testing.MapTestSuiteBuilder}.  *  * @author Kevin Bourrillion  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|MapToStringTester
specifier|public
class|class
name|MapToStringTester
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
DECL|method|testToString_minimal ()
specifier|public
name|void
name|testToString_minimal
parameter_list|()
block|{
name|assertNotNull
argument_list|(
literal|"toString() should not return null"
argument_list|,
name|getMap
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ZERO
argument_list|)
DECL|method|testToString_size0 ()
specifier|public
name|void
name|testToString_size0
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"emptyMap.toString should return {}"
argument_list|,
literal|"{}"
argument_list|,
name|getMap
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ONE
argument_list|)
DECL|method|testToString_size1 ()
specifier|public
name|void
name|testToString_size1
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"size1Map.toString should return {entry}"
argument_list|,
literal|"{"
operator|+
name|samples
operator|.
name|e0
argument_list|()
operator|+
literal|"}"
argument_list|,
name|getMap
argument_list|()
operator|.
name|toString
argument_list|()
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
DECL|method|testToStringWithNullKey ()
specifier|public
name|void
name|testToStringWithNullKey
parameter_list|()
block|{
name|initMapWithNullKey
argument_list|()
expr_stmt|;
name|testToString_formatting
argument_list|()
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
DECL|method|testToStringWithNullValue ()
specifier|public
name|void
name|testToStringWithNullValue
parameter_list|()
block|{
name|initMapWithNullValue
argument_list|()
expr_stmt|;
name|testToString_formatting
argument_list|()
expr_stmt|;
block|}
DECL|method|testToString_formatting ()
specifier|public
name|void
name|testToString_formatting
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"map.toString() incorrect"
argument_list|,
name|expectedToString
argument_list|(
name|getMap
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|)
argument_list|,
name|getMap
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|expectedToString (Set<Entry<K, V>> entries)
specifier|private
name|String
name|expectedToString
parameter_list|(
name|Set
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entries
parameter_list|)
block|{
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|reference
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
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
name|reference
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
name|reference
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

