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
import|import static
name|java
operator|.
name|util
operator|.
name|Collections
operator|.
name|singletonList
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
name|MinimalCollection
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
name|Collections
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
name|LinkedHashMap
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
comment|/**  * A generic JUnit test which tests {@code putAll} operations on a map. Can't be  * invoked directly; please see  * {@link com.google.common.collect.testing.MapTestSuiteBuilder}.  *  * @author Chris Povirk  * @author Kevin Bourrillion  */
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
DECL|class|MapPutAllTester
specifier|public
class|class
name|MapPutAllTester
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
DECL|field|containsNullKey
specifier|private
name|List
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|containsNullKey
decl_stmt|;
DECL|field|containsNullValue
specifier|private
name|List
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|containsNullValue
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
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
name|containsNullKey
operator|=
name|singletonList
argument_list|(
name|entry
argument_list|(
literal|null
argument_list|,
name|v3
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|containsNullValue
operator|=
name|singletonList
argument_list|(
name|entry
argument_list|(
name|k3
argument_list|()
argument_list|,
literal|null
argument_list|)
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
DECL|method|testPutAll_supportedNothing ()
specifier|public
name|void
name|testPutAll_supportedNothing
parameter_list|()
block|{
name|getMap
argument_list|()
operator|.
name|putAll
argument_list|(
name|emptyMap
argument_list|()
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
name|absent
operator|=
name|SUPPORTS_PUT
argument_list|)
DECL|method|testPutAll_unsupportedNothing ()
specifier|public
name|void
name|testPutAll_unsupportedNothing
parameter_list|()
block|{
try|try
block|{
name|getMap
argument_list|()
operator|.
name|putAll
argument_list|(
name|emptyMap
argument_list|()
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
name|SUPPORTS_PUT
argument_list|)
DECL|method|testPutAll_supportedNonePresent ()
specifier|public
name|void
name|testPutAll_supportedNonePresent
parameter_list|()
block|{
name|putAll
argument_list|(
name|createDisjointCollection
argument_list|()
argument_list|)
expr_stmt|;
name|expectAdded
argument_list|(
name|e3
argument_list|()
argument_list|,
name|e4
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
name|SUPPORTS_PUT
argument_list|)
DECL|method|testPutAll_unsupportedNonePresent ()
specifier|public
name|void
name|testPutAll_unsupportedNonePresent
parameter_list|()
block|{
try|try
block|{
name|putAll
argument_list|(
name|createDisjointCollection
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"putAll(nonePresent) should throw"
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
name|e3
argument_list|()
argument_list|,
name|e4
argument_list|()
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
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testPutAll_supportedSomePresent ()
specifier|public
name|void
name|testPutAll_supportedSomePresent
parameter_list|()
block|{
name|putAll
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|e3
argument_list|()
argument_list|,
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|expectAdded
argument_list|(
name|e3
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
DECL|method|testPutAllSomePresentConcurrentWithEntrySetIteration ()
specifier|public
name|void
name|testPutAllSomePresentConcurrentWithEntrySetIteration
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
name|putAll
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|e3
argument_list|()
argument_list|,
name|e0
argument_list|()
argument_list|)
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
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testPutAll_unsupportedSomePresent ()
specifier|public
name|void
name|testPutAll_unsupportedSomePresent
parameter_list|()
block|{
try|try
block|{
name|putAll
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|e3
argument_list|()
argument_list|,
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"putAll(somePresent) should throw"
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
DECL|method|testPutAll_unsupportedAllPresent ()
specifier|public
name|void
name|testPutAll_unsupportedAllPresent
parameter_list|()
block|{
try|try
block|{
name|putAll
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|e0
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
DECL|method|testPutAll_nullKeySupported ()
specifier|public
name|void
name|testPutAll_nullKeySupported
parameter_list|()
block|{
name|putAll
argument_list|(
name|containsNullKey
argument_list|)
expr_stmt|;
name|expectAdded
argument_list|(
name|containsNullKey
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
DECL|method|testPutAll_nullKeyUnsupported ()
specifier|public
name|void
name|testPutAll_nullKeyUnsupported
parameter_list|()
block|{
try|try
block|{
name|putAll
argument_list|(
name|containsNullKey
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"putAll(containsNullKey) should throw"
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
literal|"Should not contain null key after unsupported putAll(containsNullKey)"
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
DECL|method|testPutAll_nullValueSupported ()
specifier|public
name|void
name|testPutAll_nullValueSupported
parameter_list|()
block|{
name|putAll
argument_list|(
name|containsNullValue
argument_list|)
expr_stmt|;
name|expectAdded
argument_list|(
name|containsNullValue
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
DECL|method|testPutAll_nullValueUnsupported ()
specifier|public
name|void
name|testPutAll_nullValueUnsupported
parameter_list|()
block|{
try|try
block|{
name|putAll
argument_list|(
name|containsNullValue
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"putAll(containsNullValue) should throw"
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
literal|"Should not contain null value after unsupported putAll(containsNullValue)"
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
DECL|method|testPutAll_nullCollectionReference ()
specifier|public
name|void
name|testPutAll_nullCollectionReference
parameter_list|()
block|{
try|try
block|{
name|getMap
argument_list|()
operator|.
name|putAll
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"putAll(null) should throw NullPointerException"
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
DECL|method|emptyMap ()
specifier|private
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|emptyMap
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
DECL|method|putAll (Iterable<Entry<K, V>> entries)
specifier|private
name|void
name|putAll
parameter_list|(
name|Iterable
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
name|map
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
name|map
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
name|getMap
argument_list|()
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns the {@link Method} instance for {@link    * #testPutAll_nullKeyUnsupported()} so that tests can suppress it with {@code    * FeatureSpecificTestSuiteBuilder.suppressing()} until<a    * href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5045147">Sun    * bug 5045147</a> is fixed.    */
annotation|@
name|GwtIncompatible
comment|// reflection
DECL|method|getPutAllNullKeyUnsupportedMethod ()
specifier|public
specifier|static
name|Method
name|getPutAllNullKeyUnsupportedMethod
parameter_list|()
block|{
return|return
name|Helpers
operator|.
name|getMethod
argument_list|(
name|MapPutAllTester
operator|.
name|class
argument_list|,
literal|"testPutAll_nullKeyUnsupported"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

