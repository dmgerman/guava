begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.cache
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|cache
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
name|cache
operator|.
name|CacheTesting
operator|.
name|checkEmpty
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
name|cache
operator|.
name|TestingCacheLoaders
operator|.
name|identityLoader
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Arrays
operator|.
name|asList
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|DAYS
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|SECONDS
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
name|base
operator|.
name|Function
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
name|cache
operator|.
name|CacheBuilderFactory
operator|.
name|ExpirationSpec
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
name|cache
operator|.
name|LocalCache
operator|.
name|Strength
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
name|ImmutableSet
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
name|Iterables
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
name|Maps
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
name|testing
operator|.
name|EqualsTester
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutionException
import|;
end_import

begin_comment
comment|/**  * {@link LoadingCache} tests that deal with empty caches.  *  * @author mike nonemacher  */
end_comment

begin_class
DECL|class|EmptyCachesTest
specifier|public
class|class
name|EmptyCachesTest
extends|extends
name|TestCase
block|{
DECL|method|testEmpty ()
specifier|public
name|void
name|testEmpty
parameter_list|()
block|{
for|for
control|(
name|LoadingCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
range|:
name|caches
argument_list|()
control|)
block|{
name|checkEmpty
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testInvalidate_empty ()
specifier|public
name|void
name|testInvalidate_empty
parameter_list|()
block|{
for|for
control|(
name|LoadingCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
range|:
name|caches
argument_list|()
control|)
block|{
name|cache
operator|.
name|getUnchecked
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|cache
operator|.
name|getUnchecked
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|cache
operator|.
name|invalidate
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|cache
operator|.
name|invalidate
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|cache
operator|.
name|invalidate
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|checkEmpty
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testInvalidateAll_empty ()
specifier|public
name|void
name|testInvalidateAll_empty
parameter_list|()
block|{
for|for
control|(
name|LoadingCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
range|:
name|caches
argument_list|()
control|)
block|{
name|cache
operator|.
name|getUnchecked
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|cache
operator|.
name|getUnchecked
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|cache
operator|.
name|getUnchecked
argument_list|(
literal|"c"
argument_list|)
expr_stmt|;
name|cache
operator|.
name|invalidateAll
argument_list|()
expr_stmt|;
name|checkEmpty
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testEquals_null ()
specifier|public
name|void
name|testEquals_null
parameter_list|()
block|{
for|for
control|(
name|LoadingCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
range|:
name|caches
argument_list|()
control|)
block|{
name|assertFalse
argument_list|(
name|cache
operator|.
name|equals
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testEqualsAndHashCode_different ()
specifier|public
name|void
name|testEqualsAndHashCode_different
parameter_list|()
block|{
for|for
control|(
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|builder
range|:
name|cacheFactory
argument_list|()
operator|.
name|buildAllPermutations
argument_list|()
control|)
block|{
comment|// all caches should be different: instance equality
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|builder
operator|.
name|build
argument_list|(
name|identityLoader
argument_list|()
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|builder
operator|.
name|build
argument_list|(
name|identityLoader
argument_list|()
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|builder
operator|.
name|build
argument_list|(
name|identityLoader
argument_list|()
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|testGet_null ()
specifier|public
name|void
name|testGet_null
parameter_list|()
throws|throws
name|ExecutionException
block|{
for|for
control|(
name|LoadingCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
range|:
name|caches
argument_list|()
control|)
block|{
try|try
block|{
name|cache
operator|.
name|get
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected NullPointerException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
comment|// expected
block|}
name|checkEmpty
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testGetUnchecked_null ()
specifier|public
name|void
name|testGetUnchecked_null
parameter_list|()
block|{
for|for
control|(
name|LoadingCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
range|:
name|caches
argument_list|()
control|)
block|{
try|try
block|{
name|cache
operator|.
name|getUnchecked
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected NullPointerException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
comment|// expected
block|}
name|checkEmpty
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
comment|/* ---------------- Key Set -------------- */
DECL|method|testKeySet_nullToArray ()
specifier|public
name|void
name|testKeySet_nullToArray
parameter_list|()
block|{
for|for
control|(
name|LoadingCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
range|:
name|caches
argument_list|()
control|)
block|{
name|Set
argument_list|<
name|Object
argument_list|>
name|keys
init|=
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|keySet
argument_list|()
decl_stmt|;
try|try
block|{
name|keys
operator|.
name|toArray
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
comment|// expected
block|}
name|checkEmpty
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testKeySet_addNotSupported ()
specifier|public
name|void
name|testKeySet_addNotSupported
parameter_list|()
block|{
for|for
control|(
name|LoadingCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
range|:
name|caches
argument_list|()
control|)
block|{
try|try
block|{
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|add
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
comment|// expected
block|}
try|try
block|{
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|addAll
argument_list|(
name|asList
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
block|}
DECL|method|testKeySet_clear ()
specifier|public
name|void
name|testKeySet_clear
parameter_list|()
block|{
for|for
control|(
name|LoadingCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
range|:
name|caches
argument_list|()
control|)
block|{
name|warmUp
argument_list|(
name|cache
argument_list|,
literal|0
argument_list|,
literal|100
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|keys
init|=
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|keySet
argument_list|()
decl_stmt|;
name|keys
operator|.
name|clear
argument_list|()
expr_stmt|;
name|checkEmpty
argument_list|(
name|keys
argument_list|)
expr_stmt|;
name|checkEmpty
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testKeySet_empty_remove ()
specifier|public
name|void
name|testKeySet_empty_remove
parameter_list|()
block|{
for|for
control|(
name|LoadingCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
range|:
name|caches
argument_list|()
control|)
block|{
name|Set
argument_list|<
name|Object
argument_list|>
name|keys
init|=
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|keySet
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|keys
operator|.
name|remove
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|keys
operator|.
name|remove
argument_list|(
literal|6
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|keys
operator|.
name|remove
argument_list|(
operator|-
literal|6
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|keys
operator|.
name|removeAll
argument_list|(
name|asList
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|,
literal|15
argument_list|,
literal|1500
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|keys
operator|.
name|retainAll
argument_list|(
name|asList
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|,
literal|15
argument_list|,
literal|1500
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|checkEmpty
argument_list|(
name|keys
argument_list|)
expr_stmt|;
name|checkEmpty
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testKeySet_remove ()
specifier|public
name|void
name|testKeySet_remove
parameter_list|()
block|{
for|for
control|(
name|LoadingCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
range|:
name|caches
argument_list|()
control|)
block|{
name|cache
operator|.
name|getUnchecked
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|cache
operator|.
name|getUnchecked
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|keys
init|=
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|keySet
argument_list|()
decl_stmt|;
comment|// We don't know whether these are still in the cache, so we can't assert on the return
comment|// values of these removes, but the cache should be empty after the removes, regardless.
name|keys
operator|.
name|remove
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|keys
operator|.
name|remove
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|keys
operator|.
name|remove
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|keys
operator|.
name|remove
argument_list|(
literal|6
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|keys
operator|.
name|remove
argument_list|(
operator|-
literal|6
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|keys
operator|.
name|removeAll
argument_list|(
name|asList
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|,
literal|15
argument_list|,
literal|1500
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|keys
operator|.
name|retainAll
argument_list|(
name|asList
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|,
literal|15
argument_list|,
literal|1500
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|checkEmpty
argument_list|(
name|keys
argument_list|)
expr_stmt|;
name|checkEmpty
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
comment|/* ---------------- Values -------------- */
DECL|method|testValues_nullToArray ()
specifier|public
name|void
name|testValues_nullToArray
parameter_list|()
block|{
for|for
control|(
name|LoadingCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
range|:
name|caches
argument_list|()
control|)
block|{
name|Collection
argument_list|<
name|Object
argument_list|>
name|values
init|=
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|values
argument_list|()
decl_stmt|;
try|try
block|{
name|values
operator|.
name|toArray
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
comment|// expected
block|}
name|checkEmpty
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testValues_addNotSupported ()
specifier|public
name|void
name|testValues_addNotSupported
parameter_list|()
block|{
for|for
control|(
name|LoadingCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
range|:
name|caches
argument_list|()
control|)
block|{
try|try
block|{
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|add
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
comment|// expected
block|}
try|try
block|{
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|addAll
argument_list|(
name|asList
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
block|}
DECL|method|testValues_clear ()
specifier|public
name|void
name|testValues_clear
parameter_list|()
block|{
for|for
control|(
name|LoadingCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
range|:
name|caches
argument_list|()
control|)
block|{
name|warmUp
argument_list|(
name|cache
argument_list|,
literal|0
argument_list|,
literal|100
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Object
argument_list|>
name|values
init|=
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|values
argument_list|()
decl_stmt|;
name|values
operator|.
name|clear
argument_list|()
expr_stmt|;
name|checkEmpty
argument_list|(
name|values
argument_list|)
expr_stmt|;
name|checkEmpty
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testValues_empty_remove ()
specifier|public
name|void
name|testValues_empty_remove
parameter_list|()
block|{
for|for
control|(
name|LoadingCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
range|:
name|caches
argument_list|()
control|)
block|{
name|Collection
argument_list|<
name|Object
argument_list|>
name|values
init|=
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|values
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|values
operator|.
name|remove
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|values
operator|.
name|remove
argument_list|(
literal|6
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|values
operator|.
name|remove
argument_list|(
operator|-
literal|6
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|values
operator|.
name|removeAll
argument_list|(
name|asList
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|,
literal|15
argument_list|,
literal|1500
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|values
operator|.
name|retainAll
argument_list|(
name|asList
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|,
literal|15
argument_list|,
literal|1500
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|checkEmpty
argument_list|(
name|values
argument_list|)
expr_stmt|;
name|checkEmpty
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testValues_remove ()
specifier|public
name|void
name|testValues_remove
parameter_list|()
block|{
for|for
control|(
name|LoadingCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
range|:
name|caches
argument_list|()
control|)
block|{
name|cache
operator|.
name|getUnchecked
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|cache
operator|.
name|getUnchecked
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Object
argument_list|>
name|values
init|=
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|keySet
argument_list|()
decl_stmt|;
comment|// We don't know whether these are still in the cache, so we can't assert on the return
comment|// values of these removes, but the cache should be empty after the removes, regardless.
name|values
operator|.
name|remove
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|values
operator|.
name|remove
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|values
operator|.
name|remove
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|values
operator|.
name|remove
argument_list|(
literal|6
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|values
operator|.
name|remove
argument_list|(
operator|-
literal|6
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|values
operator|.
name|removeAll
argument_list|(
name|asList
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|,
literal|15
argument_list|,
literal|1500
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|values
operator|.
name|retainAll
argument_list|(
name|asList
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|,
literal|15
argument_list|,
literal|1500
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|checkEmpty
argument_list|(
name|values
argument_list|)
expr_stmt|;
name|checkEmpty
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
comment|/* ---------------- Entry Set -------------- */
DECL|method|testEntrySet_nullToArray ()
specifier|public
name|void
name|testEntrySet_nullToArray
parameter_list|()
block|{
for|for
control|(
name|LoadingCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
range|:
name|caches
argument_list|()
control|)
block|{
name|Set
argument_list|<
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|entries
init|=
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
decl_stmt|;
try|try
block|{
name|entries
operator|.
name|toArray
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
comment|// expected
block|}
name|checkEmpty
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testEntrySet_addNotSupported ()
specifier|public
name|void
name|testEntrySet_addNotSupported
parameter_list|()
block|{
for|for
control|(
name|LoadingCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
range|:
name|caches
argument_list|()
control|)
block|{
try|try
block|{
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|add
argument_list|(
name|entryOf
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
comment|// expected
block|}
try|try
block|{
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|addAll
argument_list|(
name|asList
argument_list|(
name|entryOf
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|,
name|entryOf
argument_list|(
literal|2
argument_list|,
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
block|}
DECL|method|testEntrySet_clear ()
specifier|public
name|void
name|testEntrySet_clear
parameter_list|()
block|{
for|for
control|(
name|LoadingCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
range|:
name|caches
argument_list|()
control|)
block|{
name|warmUp
argument_list|(
name|cache
argument_list|,
literal|0
argument_list|,
literal|100
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|entrySet
init|=
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
decl_stmt|;
name|entrySet
operator|.
name|clear
argument_list|()
expr_stmt|;
name|checkEmpty
argument_list|(
name|entrySet
argument_list|)
expr_stmt|;
name|checkEmpty
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testEntrySet_empty_remove ()
specifier|public
name|void
name|testEntrySet_empty_remove
parameter_list|()
block|{
for|for
control|(
name|LoadingCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
range|:
name|caches
argument_list|()
control|)
block|{
name|Set
argument_list|<
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|entrySet
init|=
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|entrySet
operator|.
name|remove
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|entrySet
operator|.
name|remove
argument_list|(
name|entryOf
argument_list|(
literal|6
argument_list|,
literal|6
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|entrySet
operator|.
name|remove
argument_list|(
name|entryOf
argument_list|(
operator|-
literal|6
argument_list|,
operator|-
literal|6
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|entrySet
operator|.
name|removeAll
argument_list|(
name|asList
argument_list|(
literal|null
argument_list|,
name|entryOf
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
argument_list|,
name|entryOf
argument_list|(
literal|15
argument_list|,
literal|15
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|entrySet
operator|.
name|retainAll
argument_list|(
name|asList
argument_list|(
literal|null
argument_list|,
name|entryOf
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
argument_list|,
name|entryOf
argument_list|(
literal|15
argument_list|,
literal|15
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|checkEmpty
argument_list|(
name|entrySet
argument_list|)
expr_stmt|;
name|checkEmpty
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testEntrySet_remove ()
specifier|public
name|void
name|testEntrySet_remove
parameter_list|()
block|{
for|for
control|(
name|LoadingCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
range|:
name|caches
argument_list|()
control|)
block|{
name|cache
operator|.
name|getUnchecked
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|cache
operator|.
name|getUnchecked
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|entrySet
init|=
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
decl_stmt|;
comment|// We don't know whether these are still in the cache, so we can't assert on the return
comment|// values of these removes, but the cache should be empty after the removes, regardless.
name|entrySet
operator|.
name|remove
argument_list|(
name|entryOf
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|entrySet
operator|.
name|remove
argument_list|(
name|entryOf
argument_list|(
literal|2
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|entrySet
operator|.
name|remove
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|entrySet
operator|.
name|remove
argument_list|(
name|entryOf
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|entrySet
operator|.
name|remove
argument_list|(
name|entryOf
argument_list|(
literal|6
argument_list|,
literal|6
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|entrySet
operator|.
name|removeAll
argument_list|(
name|asList
argument_list|(
literal|null
argument_list|,
name|entryOf
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|,
name|entryOf
argument_list|(
literal|15
argument_list|,
literal|15
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|entrySet
operator|.
name|retainAll
argument_list|(
name|asList
argument_list|(
literal|null
argument_list|,
name|entryOf
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|,
name|entryOf
argument_list|(
literal|15
argument_list|,
literal|15
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|checkEmpty
argument_list|(
name|entrySet
argument_list|)
expr_stmt|;
name|checkEmpty
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
comment|/* ---------------- Local utilities -------------- */
comment|/**    * Most of the tests in this class run against every one of these caches.    */
DECL|method|caches ()
specifier|private
name|Iterable
argument_list|<
name|LoadingCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|caches
parameter_list|()
block|{
comment|// lots of different ways to configure a LoadingCache
name|CacheBuilderFactory
name|factory
init|=
name|cacheFactory
argument_list|()
decl_stmt|;
return|return
name|Iterables
operator|.
name|transform
argument_list|(
name|factory
operator|.
name|buildAllPermutations
argument_list|()
argument_list|,
operator|new
name|Function
argument_list|<
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|,
name|LoadingCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|LoadingCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|apply
parameter_list|(
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|builder
parameter_list|)
block|{
return|return
name|builder
operator|.
name|build
argument_list|(
name|identityLoader
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|cacheFactory ()
specifier|private
name|CacheBuilderFactory
name|cacheFactory
parameter_list|()
block|{
return|return
operator|new
name|CacheBuilderFactory
argument_list|()
operator|.
name|withKeyStrengths
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
name|Strength
operator|.
name|STRONG
argument_list|,
name|Strength
operator|.
name|WEAK
argument_list|)
argument_list|)
operator|.
name|withValueStrengths
argument_list|(
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|Strength
operator|.
name|values
argument_list|()
argument_list|)
argument_list|)
operator|.
name|withConcurrencyLevels
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|4
argument_list|,
literal|16
argument_list|,
literal|64
argument_list|)
argument_list|)
operator|.
name|withMaximumSizes
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
literal|10
argument_list|,
literal|100
argument_list|,
literal|1000
argument_list|)
argument_list|)
operator|.
name|withInitialCapacities
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
literal|10
argument_list|,
literal|100
argument_list|,
literal|1000
argument_list|)
argument_list|)
operator|.
name|withExpirations
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
name|ExpirationSpec
operator|.
name|afterAccess
argument_list|(
literal|0
argument_list|,
name|SECONDS
argument_list|)
argument_list|,
name|ExpirationSpec
operator|.
name|afterAccess
argument_list|(
literal|1
argument_list|,
name|SECONDS
argument_list|)
argument_list|,
name|ExpirationSpec
operator|.
name|afterAccess
argument_list|(
literal|1
argument_list|,
name|DAYS
argument_list|)
argument_list|,
name|ExpirationSpec
operator|.
name|afterWrite
argument_list|(
literal|0
argument_list|,
name|SECONDS
argument_list|)
argument_list|,
name|ExpirationSpec
operator|.
name|afterWrite
argument_list|(
literal|1
argument_list|,
name|SECONDS
argument_list|)
argument_list|,
name|ExpirationSpec
operator|.
name|afterWrite
argument_list|(
literal|1
argument_list|,
name|DAYS
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|warmUp (LoadingCache<Object, Object> cache, int minimum, int maximum)
specifier|private
name|void
name|warmUp
parameter_list|(
name|LoadingCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
parameter_list|,
name|int
name|minimum
parameter_list|,
name|int
name|maximum
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
name|minimum
init|;
name|i
operator|<
name|maximum
condition|;
name|i
operator|++
control|)
block|{
name|cache
operator|.
name|getUnchecked
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|entryOf (Object key, Object value)
specifier|private
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entryOf
parameter_list|(
name|Object
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

