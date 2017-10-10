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
name|SEVERAL
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
name|SUPPORTS_PUT
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

begin_comment
comment|/** Tester for {@code BiMap.put} and {@code BiMap.forcePut}. */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|BiMapPutTester
specifier|public
class|class
name|BiMapPutTester
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractBiMapTester
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_PUT
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ZERO
argument_list|)
DECL|method|testPutWithSameValueFails ()
specifier|public
name|void
name|testPutWithSameValueFails
parameter_list|()
block|{
name|getMap
argument_list|()
operator|.
name|put
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v0
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|getMap
argument_list|()
operator|.
name|put
argument_list|(
name|k1
argument_list|()
argument_list|,
name|v0
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{
comment|// success
block|}
comment|// verify that the bimap is unchanged
name|expectAdded
argument_list|(
name|e0
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_PUT
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ZERO
argument_list|)
DECL|method|testPutPresentKeyDifferentValue ()
specifier|public
name|void
name|testPutPresentKeyDifferentValue
parameter_list|()
block|{
name|getMap
argument_list|()
operator|.
name|put
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v0
argument_list|()
argument_list|)
expr_stmt|;
name|getMap
argument_list|()
operator|.
name|put
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v1
argument_list|()
argument_list|)
expr_stmt|;
comment|// verify that the bimap is changed, and that the old inverse mapping
comment|// from v1 -> v0 is deleted
name|expectContents
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v1
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_PUT
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ZERO
argument_list|)
DECL|method|putDistinctKeysDistinctValues ()
specifier|public
name|void
name|putDistinctKeysDistinctValues
parameter_list|()
block|{
name|getMap
argument_list|()
operator|.
name|put
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v0
argument_list|()
argument_list|)
expr_stmt|;
name|getMap
argument_list|()
operator|.
name|put
argument_list|(
name|k1
argument_list|()
argument_list|,
name|v1
argument_list|()
argument_list|)
expr_stmt|;
name|expectAdded
argument_list|(
name|e0
argument_list|()
argument_list|,
name|e1
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_PUT
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ONE
argument_list|)
DECL|method|testForcePutKeyPresent ()
specifier|public
name|void
name|testForcePutKeyPresent
parameter_list|()
block|{
name|getMap
argument_list|()
operator|.
name|forcePut
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v1
argument_list|()
argument_list|)
expr_stmt|;
name|expectContents
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v1
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|getMap
argument_list|()
operator|.
name|containsValue
argument_list|(
name|v0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|getMap
argument_list|()
operator|.
name|inverse
argument_list|()
operator|.
name|get
argument_list|(
name|v0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|k0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_PUT
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ONE
argument_list|)
DECL|method|testForcePutValuePresent ()
specifier|public
name|void
name|testForcePutValuePresent
parameter_list|()
block|{
name|getMap
argument_list|()
operator|.
name|forcePut
argument_list|(
name|k1
argument_list|()
argument_list|,
name|v0
argument_list|()
argument_list|)
expr_stmt|;
name|expectContents
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|k1
argument_list|()
argument_list|,
name|v0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|k1
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
name|v0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|getMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|k0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_PUT
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
DECL|method|testForcePutKeyAndValuePresent ()
specifier|public
name|void
name|testForcePutKeyAndValuePresent
parameter_list|()
block|{
name|getMap
argument_list|()
operator|.
name|forcePut
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v1
argument_list|()
argument_list|)
expr_stmt|;
name|expectContents
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v1
argument_list|()
argument_list|)
argument_list|,
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|k2
argument_list|()
argument_list|,
name|v2
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|getMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|getMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|k1
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|getMap
argument_list|()
operator|.
name|containsValue
argument_list|(
name|v0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
block|{
name|SUPPORTS_PUT
block|,
name|ALLOWS_NULL_KEYS
block|}
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ONE
argument_list|)
DECL|method|testForcePutNullKeyPresent ()
specifier|public
name|void
name|testForcePutNullKeyPresent
parameter_list|()
block|{
name|initMapWithNullKey
argument_list|()
expr_stmt|;
name|getMap
argument_list|()
operator|.
name|forcePut
argument_list|(
literal|null
argument_list|,
name|v1
argument_list|()
argument_list|)
expr_stmt|;
name|expectContents
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
operator|(
name|K
operator|)
literal|null
argument_list|,
name|v1
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|getMap
argument_list|()
operator|.
name|containsValue
argument_list|(
name|v0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getMap
argument_list|()
operator|.
name|containsValue
argument_list|(
name|v1
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getMap
argument_list|()
operator|.
name|inverse
argument_list|()
operator|.
name|containsKey
argument_list|(
name|v1
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|getMap
argument_list|()
operator|.
name|inverse
argument_list|()
operator|.
name|get
argument_list|(
name|v1
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|v1
argument_list|()
argument_list|,
name|getMap
argument_list|()
operator|.
name|get
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
block|{
name|SUPPORTS_PUT
block|,
name|ALLOWS_NULL_VALUES
block|}
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ONE
argument_list|)
DECL|method|testForcePutNullValuePresent ()
specifier|public
name|void
name|testForcePutNullValuePresent
parameter_list|()
block|{
name|initMapWithNullValue
argument_list|()
expr_stmt|;
name|getMap
argument_list|()
operator|.
name|forcePut
argument_list|(
name|k1
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|expectContents
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|k1
argument_list|()
argument_list|,
operator|(
name|V
operator|)
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|getMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|k0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|k1
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getMap
argument_list|()
operator|.
name|inverse
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|getMap
argument_list|()
operator|.
name|get
argument_list|(
name|k1
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|k1
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
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// nb: inverse is run through its own entire suite
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_PUT
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ZERO
argument_list|)
DECL|method|testInversePut ()
specifier|public
name|void
name|testInversePut
parameter_list|()
block|{
name|getMap
argument_list|()
operator|.
name|put
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v0
argument_list|()
argument_list|)
expr_stmt|;
name|getMap
argument_list|()
operator|.
name|inverse
argument_list|()
operator|.
name|put
argument_list|(
name|v1
argument_list|()
argument_list|,
name|k1
argument_list|()
argument_list|)
expr_stmt|;
name|expectAdded
argument_list|(
name|e0
argument_list|()
argument_list|,
name|e1
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

