begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|FAILS_FAST_ON_CONCURRENT_MODIFICATION
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
name|ConcurrentModificationException
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
comment|/**  * A generic JUnit test which tests {@code put} operations on a map. Can't be  * invoked directly; please see  * {@link com.google.common.collect.testing.MapTestSuiteBuilder}.  *  *<p>This class is GWT compatible.  *  * @author Chris Povirk  * @author Kevin Bourrillion  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// too many "unchecked generic array creations"
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|MapPutTester
specifier|public
class|class
name|MapPutTester
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
DECL|field|nullKeyEntry
specifier|private
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|nullKeyEntry
decl_stmt|;
DECL|field|nullValueEntry
specifier|private
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|nullValueEntry
decl_stmt|;
DECL|field|nullKeyValueEntry
specifier|private
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|nullKeyValueEntry
decl_stmt|;
DECL|field|presentKeyNullValueEntry
specifier|private
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|presentKeyNullValueEntry
decl_stmt|;
DECL|method|setUp ()
annotation|@
name|Override
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|nullKeyEntry
operator|=
name|entry
argument_list|(
literal|null
argument_list|,
name|samples
operator|.
name|e3
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|nullValueEntry
operator|=
name|entry
argument_list|(
name|samples
operator|.
name|e3
operator|.
name|getKey
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|nullKeyValueEntry
operator|=
name|entry
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|presentKeyNullValueEntry
operator|=
name|entry
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_PUT
argument_list|)
DECL|method|testPut_supportedNotPresent ()
specifier|public
name|void
name|testPut_supportedNotPresent
parameter_list|()
block|{
name|assertNull
argument_list|(
literal|"put(notPresent, value) should return null"
argument_list|,
name|put
argument_list|(
name|samples
operator|.
name|e3
argument_list|)
argument_list|)
expr_stmt|;
name|expectAdded
argument_list|(
name|samples
operator|.
name|e3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
block|{
name|FAILS_FAST_ON_CONCURRENT_MODIFICATION
block|,
name|SUPPORTS_PUT
block|}
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testPutAbsentConcurrentWithEntrySetIteration ()
specifier|public
name|void
name|testPutAbsentConcurrentWithEntrySetIteration
parameter_list|()
block|{
try|try
block|{
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|iterator
init|=
name|getMap
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|put
argument_list|(
name|samples
operator|.
name|e3
argument_list|)
expr_stmt|;
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected ConcurrentModificationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConcurrentModificationException
name|expected
parameter_list|)
block|{
comment|// success
block|}
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
block|{
name|FAILS_FAST_ON_CONCURRENT_MODIFICATION
block|,
name|SUPPORTS_PUT
block|}
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testPutAbsentConcurrentWithKeySetIteration ()
specifier|public
name|void
name|testPutAbsentConcurrentWithKeySetIteration
parameter_list|()
block|{
try|try
block|{
name|Iterator
argument_list|<
name|K
argument_list|>
name|iterator
init|=
name|getMap
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|put
argument_list|(
name|samples
operator|.
name|e3
argument_list|)
expr_stmt|;
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected ConcurrentModificationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConcurrentModificationException
name|expected
parameter_list|)
block|{
comment|// success
block|}
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
block|{
name|FAILS_FAST_ON_CONCURRENT_MODIFICATION
block|,
name|SUPPORTS_PUT
block|}
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testPutAbsentConcurrentWithValueIteration ()
specifier|public
name|void
name|testPutAbsentConcurrentWithValueIteration
parameter_list|()
block|{
try|try
block|{
name|Iterator
argument_list|<
name|V
argument_list|>
name|iterator
init|=
name|getMap
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|put
argument_list|(
name|samples
operator|.
name|e3
argument_list|)
expr_stmt|;
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected ConcurrentModificationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConcurrentModificationException
name|expected
parameter_list|)
block|{
comment|// success
block|}
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_PUT
argument_list|)
DECL|method|testPut_unsupportedNotPresent ()
specifier|public
name|void
name|testPut_unsupportedNotPresent
parameter_list|()
block|{
try|try
block|{
name|put
argument_list|(
name|samples
operator|.
name|e3
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"put(notPresent, value) should throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
name|expectUnchanged
argument_list|()
expr_stmt|;
name|expectMissing
argument_list|(
name|samples
operator|.
name|e3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_PUT
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testPut_unsupportedPresentExistingValue ()
specifier|public
name|void
name|testPut_unsupportedPresentExistingValue
parameter_list|()
block|{
try|try
block|{
name|assertEquals
argument_list|(
literal|"put(present, existingValue) should return present or throw"
argument_list|,
name|samples
operator|.
name|e0
operator|.
name|getValue
argument_list|()
argument_list|,
name|put
argument_list|(
name|samples
operator|.
name|e0
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|tolerated
parameter_list|)
block|{     }
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_PUT
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testPut_unsupportedPresentDifferentValue ()
specifier|public
name|void
name|testPut_unsupportedPresentDifferentValue
parameter_list|()
block|{
try|try
block|{
name|getMap
argument_list|()
operator|.
name|put
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
argument_list|,
name|samples
operator|.
name|e3
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"put(present, differentValue) should throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
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
DECL|method|testPut_nullKeySupportedNotPresent ()
specifier|public
name|void
name|testPut_nullKeySupportedNotPresent
parameter_list|()
block|{
name|assertNull
argument_list|(
literal|"put(null, value) should return null"
argument_list|,
name|put
argument_list|(
name|nullKeyEntry
argument_list|)
argument_list|)
expr_stmt|;
name|expectAdded
argument_list|(
name|nullKeyEntry
argument_list|)
expr_stmt|;
block|}
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
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testPut_nullKeySupportedPresent ()
specifier|public
name|void
name|testPut_nullKeySupportedPresent
parameter_list|()
block|{
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newEntry
init|=
name|entry
argument_list|(
literal|null
argument_list|,
name|samples
operator|.
name|e3
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|initMapWithNullKey
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"put(present, value) should return the associated value"
argument_list|,
name|getValueForNullKey
argument_list|()
argument_list|,
name|put
argument_list|(
name|newEntry
argument_list|)
argument_list|)
expr_stmt|;
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|expected
init|=
name|createArrayWithNullKey
argument_list|()
decl_stmt|;
name|expected
index|[
name|getNullLocation
argument_list|()
index|]
operator|=
name|newEntry
expr_stmt|;
name|expectContents
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|value
operator|=
name|SUPPORTS_PUT
argument_list|,
name|absent
operator|=
name|ALLOWS_NULL_KEYS
argument_list|)
DECL|method|testPut_nullKeyUnsupported ()
specifier|public
name|void
name|testPut_nullKeyUnsupported
parameter_list|()
block|{
try|try
block|{
name|put
argument_list|(
name|nullKeyEntry
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"put(null, value) should throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
name|expectUnchanged
argument_list|()
expr_stmt|;
name|expectNullKeyMissingWhenNullKeysUnsupported
argument_list|(
literal|"Should not contain null key after unsupported put(null, value)"
argument_list|)
expr_stmt|;
block|}
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
DECL|method|testPut_nullValueSupported ()
specifier|public
name|void
name|testPut_nullValueSupported
parameter_list|()
block|{
name|assertNull
argument_list|(
literal|"put(key, null) should return null"
argument_list|,
name|put
argument_list|(
name|nullValueEntry
argument_list|)
argument_list|)
expr_stmt|;
name|expectAdded
argument_list|(
name|nullValueEntry
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|value
operator|=
name|SUPPORTS_PUT
argument_list|,
name|absent
operator|=
name|ALLOWS_NULL_VALUES
argument_list|)
DECL|method|testPut_nullValueUnsupported ()
specifier|public
name|void
name|testPut_nullValueUnsupported
parameter_list|()
block|{
try|try
block|{
name|put
argument_list|(
name|nullValueEntry
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"put(key, null) should throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
name|expectUnchanged
argument_list|()
expr_stmt|;
name|expectNullValueMissingWhenNullValuesUnsupported
argument_list|(
literal|"Should not contain null value after unsupported put(key, null)"
argument_list|)
expr_stmt|;
block|}
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
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testPut_replaceWithNullValueSupported ()
specifier|public
name|void
name|testPut_replaceWithNullValueSupported
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"put(present, null) should return the associated value"
argument_list|,
name|samples
operator|.
name|e0
operator|.
name|getValue
argument_list|()
argument_list|,
name|put
argument_list|(
name|presentKeyNullValueEntry
argument_list|)
argument_list|)
expr_stmt|;
name|expectReplacement
argument_list|(
name|presentKeyNullValueEntry
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|value
operator|=
name|SUPPORTS_PUT
argument_list|,
name|absent
operator|=
name|ALLOWS_NULL_VALUES
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testPut_replaceWithNullValueUnsupported ()
specifier|public
name|void
name|testPut_replaceWithNullValueUnsupported
parameter_list|()
block|{
try|try
block|{
name|put
argument_list|(
name|presentKeyNullValueEntry
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"put(present, null) should throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
name|expectUnchanged
argument_list|()
expr_stmt|;
name|expectNullValueMissingWhenNullValuesUnsupported
argument_list|(
literal|"Should not contain null after unsupported put(present, null)"
argument_list|)
expr_stmt|;
block|}
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
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testPut_replaceNullValueWithNullSupported ()
specifier|public
name|void
name|testPut_replaceNullValueWithNullSupported
parameter_list|()
block|{
name|initMapWithNullValue
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
literal|"put(present, null) should return the associated value (null)"
argument_list|,
name|getMap
argument_list|()
operator|.
name|put
argument_list|(
name|getKeyForNullValue
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|expectContents
argument_list|(
name|createArrayWithNullValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testPut_replaceNullValueWithNonNullSupported ()
specifier|public
name|void
name|testPut_replaceNullValueWithNonNullSupported
parameter_list|()
block|{
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newEntry
init|=
name|entry
argument_list|(
name|getKeyForNullValue
argument_list|()
argument_list|,
name|samples
operator|.
name|e3
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|initMapWithNullValue
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
literal|"put(present, value) should return the associated value (null)"
argument_list|,
name|put
argument_list|(
name|newEntry
argument_list|)
argument_list|)
expr_stmt|;
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|expected
init|=
name|createArrayWithNullValue
argument_list|()
decl_stmt|;
name|expected
index|[
name|getNullLocation
argument_list|()
index|]
operator|=
name|newEntry
expr_stmt|;
name|expectContents
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
block|{
name|SUPPORTS_PUT
block|,
name|ALLOWS_NULL_KEYS
block|,
name|ALLOWS_NULL_VALUES
block|}
argument_list|)
DECL|method|testPut_nullKeyAndValueSupported ()
specifier|public
name|void
name|testPut_nullKeyAndValueSupported
parameter_list|()
block|{
name|assertNull
argument_list|(
literal|"put(null, null) should return null"
argument_list|,
name|put
argument_list|(
name|nullKeyValueEntry
argument_list|)
argument_list|)
expr_stmt|;
name|expectAdded
argument_list|(
name|nullKeyValueEntry
argument_list|)
expr_stmt|;
block|}
DECL|method|put (Map.Entry<K, V> entry)
specifier|private
name|V
name|put
parameter_list|(
name|Map
operator|.
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
name|getMap
argument_list|()
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
return|;
block|}
block|}
end_class

end_unit

