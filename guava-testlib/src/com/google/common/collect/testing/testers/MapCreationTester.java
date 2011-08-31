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
name|REJECTS_DUPLICATES_AT_CREATION
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
name|Arrays
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
comment|/**  * A generic JUnit test which tests creation (typically through a constructor or  * static factory method) of a map. Can't be invoked directly; please see  * {@link com.google.common.collect.testing.MapTestSuiteBuilder}.  *  *<p>This class is GWT compatible.  *  * @author Chris Povirk  * @author Kevin Bourrillion  */
end_comment

begin_class
DECL|class|MapCreationTester
specifier|public
class|class
name|MapCreationTester
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
name|MapFeature
operator|.
name|Require
argument_list|(
name|ALLOWS_NULL_KEYS
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
DECL|method|testCreateWithNullKeySupported ()
specifier|public
name|void
name|testCreateWithNullKeySupported
parameter_list|()
block|{
name|initMapWithNullKey
argument_list|()
expr_stmt|;
name|expectContents
argument_list|(
name|createArrayWithNullKey
argument_list|()
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
name|ALLOWS_NULL_KEYS
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
DECL|method|testCreateWithNullKeyUnsupported ()
specifier|public
name|void
name|testCreateWithNullKeyUnsupported
parameter_list|()
block|{
try|try
block|{
name|initMapWithNullKey
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Creating a map containing a null key should fail"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
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
DECL|method|testCreateWithNullValueSupported ()
specifier|public
name|void
name|testCreateWithNullValueSupported
parameter_list|()
block|{
name|initMapWithNullValue
argument_list|()
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
DECL|method|testCreateWithNullValueUnsupported ()
specifier|public
name|void
name|testCreateWithNullValueUnsupported
parameter_list|()
block|{
try|try
block|{
name|initMapWithNullValue
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Creating a map containing a null value should fail"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
block|{
name|ALLOWS_NULL_KEYS
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
DECL|method|testCreateWithNullKeyAndValueSupported ()
specifier|public
name|void
name|testCreateWithNullKeyAndValueSupported
parameter_list|()
block|{
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|entries
init|=
name|createSamplesArray
argument_list|()
decl_stmt|;
name|entries
index|[
name|getNullLocation
argument_list|()
index|]
operator|=
name|entry
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|resetMap
argument_list|(
name|entries
argument_list|)
expr_stmt|;
name|expectContents
argument_list|(
name|entries
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
name|ALLOWS_NULL_KEYS
argument_list|,
name|absent
operator|=
name|REJECTS_DUPLICATES_AT_CREATION
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
block|{
name|ZERO
block|,
name|ONE
block|}
argument_list|)
DECL|method|testCreateWithDuplicates_nullDuplicatesNotRejected ()
specifier|public
name|void
name|testCreateWithDuplicates_nullDuplicatesNotRejected
parameter_list|()
block|{
name|expectFirstRemoved
argument_list|(
name|getEntriesMultipleNullKeys
argument_list|()
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
name|REJECTS_DUPLICATES_AT_CREATION
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
block|{
name|ZERO
block|,
name|ONE
block|}
argument_list|)
DECL|method|testCreateWithDuplicates_nonNullDuplicatesNotRejected ()
specifier|public
name|void
name|testCreateWithDuplicates_nonNullDuplicatesNotRejected
parameter_list|()
block|{
name|expectFirstRemoved
argument_list|(
name|getEntriesMultipleNonNullKeys
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
name|ALLOWS_NULL_KEYS
block|,
name|REJECTS_DUPLICATES_AT_CREATION
block|}
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
block|{
name|ZERO
block|,
name|ONE
block|}
argument_list|)
DECL|method|testCreateWithDuplicates_nullDuplicatesRejected ()
specifier|public
name|void
name|testCreateWithDuplicates_nullDuplicatesRejected
parameter_list|()
block|{
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|entries
init|=
name|getEntriesMultipleNullKeys
argument_list|()
decl_stmt|;
try|try
block|{
name|resetMap
argument_list|(
name|entries
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should reject duplicate null elements at creation"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|REJECTS_DUPLICATES_AT_CREATION
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
block|{
name|ZERO
block|,
name|ONE
block|}
argument_list|)
DECL|method|testCreateWithDuplicates_nonNullDuplicatesRejected ()
specifier|public
name|void
name|testCreateWithDuplicates_nonNullDuplicatesRejected
parameter_list|()
block|{
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|entries
init|=
name|getEntriesMultipleNonNullKeys
argument_list|()
decl_stmt|;
try|try
block|{
name|resetMap
argument_list|(
name|entries
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should reject duplicate non-null elements at creation"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|getEntriesMultipleNullKeys ()
specifier|private
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|getEntriesMultipleNullKeys
parameter_list|()
block|{
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|entries
init|=
name|createArrayWithNullKey
argument_list|()
decl_stmt|;
name|entries
index|[
literal|0
index|]
operator|=
name|entries
index|[
name|getNullLocation
argument_list|()
index|]
expr_stmt|;
return|return
name|entries
return|;
block|}
DECL|method|getEntriesMultipleNonNullKeys ()
specifier|private
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|getEntriesMultipleNonNullKeys
parameter_list|()
block|{
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|entries
init|=
name|createSamplesArray
argument_list|()
decl_stmt|;
name|entries
index|[
literal|0
index|]
operator|=
name|samples
operator|.
name|e1
expr_stmt|;
return|return
name|entries
return|;
block|}
DECL|method|expectFirstRemoved (Entry<K, V>[] entries)
specifier|private
name|void
name|expectFirstRemoved
parameter_list|(
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|entries
parameter_list|)
block|{
name|resetMap
argument_list|(
name|entries
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|expectedWithDuplicateRemoved
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|entries
argument_list|)
operator|.
name|subList
argument_list|(
literal|1
argument_list|,
name|getNumElements
argument_list|()
argument_list|)
decl_stmt|;
name|expectContents
argument_list|(
name|expectedWithDuplicateRemoved
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

