begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ALLOWS_NULL_QUERIES
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
name|SUPPORTS_REMOVE
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
name|WrongType
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
operator|.
name|Entry
import|;
end_import

begin_comment
comment|/**  * A generic JUnit test which tests {@code remove} operations on a map. Can't be  * invoked directly; please see  * {@link com.google.common.collect.testing.MapTestSuiteBuilder}.  *  *<p>This class is GWT compatible.  *  * @author George van den Driessche  * @author Chris Povirk  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// too many "unchecked generic array creations"
DECL|class|MapRemoveTester
specifier|public
class|class
name|MapRemoveTester
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
name|SUPPORTS_REMOVE
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
DECL|method|testRemove_present ()
specifier|public
name|void
name|testRemove_present
parameter_list|()
block|{
name|int
name|initialSize
init|=
name|getMap
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"remove(present) should return the associated value"
argument_list|,
name|samples
operator|.
name|e0
operator|.
name|getValue
argument_list|()
argument_list|,
name|getMap
argument_list|()
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"remove(present) should decrease a map's size by one."
argument_list|,
name|initialSize
operator|-
literal|1
argument_list|,
name|getMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|expectMissing
argument_list|(
name|samples
operator|.
name|e0
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
name|SUPPORTS_REMOVE
block|}
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
DECL|method|testRemovePresentConcurrentWithEntrySetIteration ()
specifier|public
name|void
name|testRemovePresentConcurrentWithEntrySetIteration
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
name|getMap
argument_list|()
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
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
name|SUPPORTS_REMOVE
block|}
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
DECL|method|testRemovePresentConcurrentWithKeySetIteration ()
specifier|public
name|void
name|testRemovePresentConcurrentWithKeySetIteration
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
name|getMap
argument_list|()
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
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
name|SUPPORTS_REMOVE
block|}
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
DECL|method|testRemovePresentConcurrentWithValuesIteration ()
specifier|public
name|void
name|testRemovePresentConcurrentWithValuesIteration
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
name|getMap
argument_list|()
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
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
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testRemove_notPresent ()
specifier|public
name|void
name|testRemove_notPresent
parameter_list|()
block|{
name|assertNull
argument_list|(
literal|"remove(notPresent) should return null"
argument_list|,
name|getMap
argument_list|()
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e3
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
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
name|SUPPORTS_REMOVE
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
DECL|method|testRemove_nullPresent ()
specifier|public
name|void
name|testRemove_nullPresent
parameter_list|()
block|{
name|initMapWithNullKey
argument_list|()
expr_stmt|;
name|int
name|initialSize
init|=
name|getMap
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"remove(null) should return the associated value"
argument_list|,
name|getValueForNullKey
argument_list|()
argument_list|,
name|getMap
argument_list|()
operator|.
name|remove
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"remove(present) should decrease a map's size by one."
argument_list|,
name|initialSize
operator|-
literal|1
argument_list|,
name|getMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|expectMissing
argument_list|(
name|entry
argument_list|(
literal|null
argument_list|,
name|getValueForNullKey
argument_list|()
argument_list|)
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
name|SUPPORTS_REMOVE
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
DECL|method|testRemove_unsupported ()
specifier|public
name|void
name|testRemove_unsupported
parameter_list|()
block|{
try|try
block|{
name|getMap
argument_list|()
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"remove(present) should throw UnsupportedOperationException"
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
name|assertEquals
argument_list|(
literal|"remove(present) should not remove the element"
argument_list|,
name|samples
operator|.
name|e0
operator|.
name|getValue
argument_list|()
argument_list|,
name|get
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
argument_list|)
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
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testRemove_unsupportedNotPresent ()
specifier|public
name|void
name|testRemove_unsupportedNotPresent
parameter_list|()
block|{
try|try
block|{
name|assertNull
argument_list|(
literal|"remove(notPresent) should return null or throw "
operator|+
literal|"UnsupportedOperationException"
argument_list|,
name|getMap
argument_list|()
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e3
operator|.
name|getKey
argument_list|()
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
name|value
operator|=
name|SUPPORTS_REMOVE
argument_list|,
name|absent
operator|=
name|ALLOWS_NULL_QUERIES
argument_list|)
DECL|method|testRemove_nullQueriesNotSupported ()
specifier|public
name|void
name|testRemove_nullQueriesNotSupported
parameter_list|()
block|{
try|try
block|{
name|assertNull
argument_list|(
literal|"remove(null) should return null or throw "
operator|+
literal|"NullPointerException"
argument_list|,
name|getMap
argument_list|()
operator|.
name|remove
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
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
block|{
name|SUPPORTS_REMOVE
block|,
name|ALLOWS_NULL_QUERIES
block|}
argument_list|)
DECL|method|testRemove_nullSupportedMissing ()
specifier|public
name|void
name|testRemove_nullSupportedMissing
parameter_list|()
block|{
name|assertNull
argument_list|(
literal|"remove(null) should return null"
argument_list|,
name|getMap
argument_list|()
operator|.
name|remove
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testRemove_wrongType ()
specifier|public
name|void
name|testRemove_wrongType
parameter_list|()
block|{
try|try
block|{
name|assertNull
argument_list|(
name|getMap
argument_list|()
operator|.
name|remove
argument_list|(
name|WrongType
operator|.
name|VALUE
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|tolerated
parameter_list|)
block|{     }
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

