begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2011 Google Inc. All Rights Reserved.
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
name|CacheTesting
operator|.
name|checkValidState
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
import|import static
name|org
operator|.
name|junit
operator|.
name|contrib
operator|.
name|truth
operator|.
name|Truth
operator|.
name|ASSERT
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
name|CustomConcurrentHashMap
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
name|ImmutableMap
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
name|Iterators
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
name|Lists
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
comment|/**  * {@link Cache} tests that deal with caches that actually contain some key-value mappings.  *  * @author mike nonemacher  */
end_comment

begin_class
DECL|class|PopulatedCachesTest
specifier|public
class|class
name|PopulatedCachesTest
extends|extends
name|TestCase
block|{
comment|// we use integers as keys; make sure the range covers some values that ARE cached by
comment|// Integer.valueOf(int), and some that are not cached. (127 is the highest cached value.)
DECL|field|WARMUP_MIN
specifier|static
specifier|final
name|int
name|WARMUP_MIN
init|=
literal|120
decl_stmt|;
DECL|field|WARMUP_MAX
specifier|static
specifier|final
name|int
name|WARMUP_MAX
init|=
literal|135
decl_stmt|;
DECL|field|WARMUP_SIZE
specifier|static
specifier|final
name|int
name|WARMUP_SIZE
init|=
name|WARMUP_MAX
operator|-
name|WARMUP_MIN
decl_stmt|;
DECL|method|testSize_populated ()
specifier|public
name|void
name|testSize_populated
parameter_list|()
block|{
for|for
control|(
name|Cache
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
comment|// don't let the entries get GCed
name|List
argument_list|<
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|warmed
init|=
name|warmUp
argument_list|(
name|cache
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|WARMUP_SIZE
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertMapSize
argument_list|(
name|cache
operator|.
name|asMap
argument_list|()
argument_list|,
name|WARMUP_SIZE
argument_list|)
expr_stmt|;
name|checkValidState
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testContainsKey_found ()
specifier|public
name|void
name|testContainsKey_found
parameter_list|()
block|{
for|for
control|(
name|Cache
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
comment|// don't let the entries get GCed
name|List
argument_list|<
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|warmed
init|=
name|warmUp
argument_list|(
name|cache
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|WARMUP_MIN
init|;
name|i
operator|<
name|WARMUP_MAX
condition|;
name|i
operator|++
control|)
block|{
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
init|=
name|warmed
operator|.
name|get
argument_list|(
name|i
operator|-
name|WARMUP_MIN
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|containsValue
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// this getUnchecked() call shouldn't be a cache miss; verified below
name|assertEquals
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|cache
operator|.
name|getUnchecked
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|WARMUP_SIZE
argument_list|,
name|cache
operator|.
name|stats
argument_list|()
operator|.
name|missCount
argument_list|()
argument_list|)
expr_stmt|;
name|checkValidState
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testPut_fails ()
specifier|public
name|void
name|testPut_fails
parameter_list|()
block|{
for|for
control|(
name|Cache
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
comment|// don't let the entries get GCed
name|List
argument_list|<
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|warmed
init|=
name|warmUp
argument_list|(
name|cache
argument_list|)
decl_stmt|;
try|try
block|{
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|put
argument_list|(
operator|-
literal|1
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"asMap().put() should have triggered UnsupportedOperationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{       }
name|checkValidState
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testPutIfAbsent_fails ()
specifier|public
name|void
name|testPutIfAbsent_fails
parameter_list|()
block|{
for|for
control|(
name|Cache
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
comment|// don't let the entries get GCed
name|List
argument_list|<
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|warmed
init|=
name|warmUp
argument_list|(
name|cache
argument_list|)
decl_stmt|;
try|try
block|{
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|putIfAbsent
argument_list|(
operator|-
literal|1
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"asMap().putIfAbsent() should have triggered UnsupportedOperationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{       }
name|checkValidState
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testPutAll_fails ()
specifier|public
name|void
name|testPutAll_fails
parameter_list|()
block|{
for|for
control|(
name|Cache
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
comment|// don't let the entries get GCed
name|List
argument_list|<
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|warmed
init|=
name|warmUp
argument_list|(
name|cache
argument_list|)
decl_stmt|;
try|try
block|{
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|putAll
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"asMap().putAll() should have triggered UnsupportedOperationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{       }
name|checkValidState
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testReplace_fails ()
specifier|public
name|void
name|testReplace_fails
parameter_list|()
block|{
for|for
control|(
name|Cache
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
comment|// don't let the entries get GCed
name|List
argument_list|<
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|warmed
init|=
name|warmUp
argument_list|(
name|cache
argument_list|)
decl_stmt|;
try|try
block|{
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|replace
argument_list|(
name|warmed
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getKey
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"asMap().replace() should have triggered UnsupportedOperationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{       }
try|try
block|{
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|replace
argument_list|(
name|warmed
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getKey
argument_list|()
argument_list|,
name|warmed
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"asMap().replace() should have triggered UnsupportedOperationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{       }
name|checkValidState
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testRemove_byKey ()
specifier|public
name|void
name|testRemove_byKey
parameter_list|()
block|{
for|for
control|(
name|Cache
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
comment|// don't let the entries get GCed
name|List
argument_list|<
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|warmed
init|=
name|warmUp
argument_list|(
name|cache
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|WARMUP_MIN
init|;
name|i
operator|<
name|WARMUP_MAX
condition|;
name|i
operator|++
control|)
block|{
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
init|=
name|warmed
operator|.
name|get
argument_list|(
name|i
operator|-
name|WARMUP_MIN
argument_list|)
decl_stmt|;
name|Object
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|remove
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|remove
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|checkEmpty
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testRemove_byKeyAndValue ()
specifier|public
name|void
name|testRemove_byKeyAndValue
parameter_list|()
block|{
for|for
control|(
name|Cache
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
comment|// don't let the entries get GCed
name|List
argument_list|<
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|warmed
init|=
name|warmUp
argument_list|(
name|cache
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|WARMUP_MIN
init|;
name|i
operator|<
name|WARMUP_MAX
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|key
init|=
name|warmed
operator|.
name|get
argument_list|(
name|i
operator|-
name|WARMUP_MIN
argument_list|)
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|warmed
operator|.
name|get
argument_list|(
name|i
operator|-
name|WARMUP_MIN
argument_list|)
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|remove
argument_list|(
name|key
argument_list|,
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|remove
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|remove
argument_list|(
name|key
argument_list|,
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|checkEmpty
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testKeySet_populated ()
specifier|public
name|void
name|testKeySet_populated
parameter_list|()
block|{
for|for
control|(
name|Cache
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
name|List
argument_list|<
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|warmed
init|=
name|warmUp
argument_list|(
name|cache
argument_list|)
decl_stmt|;
name|Object
index|[]
name|expectedArray
init|=
name|Maps
operator|.
name|newHashMap
argument_list|(
name|cache
operator|.
name|asMap
argument_list|()
argument_list|)
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|Object
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|keys
argument_list|)
operator|.
name|hasContentsAnyOrder
argument_list|(
name|expectedArray
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|asList
argument_list|(
name|keys
operator|.
name|toArray
argument_list|()
argument_list|)
argument_list|)
operator|.
name|hasContentsAnyOrder
argument_list|(
name|expectedArray
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|asList
argument_list|(
name|keys
operator|.
name|toArray
argument_list|(
operator|new
name|Object
index|[
name|cache
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
argument_list|)
operator|.
name|hasContentsAnyOrder
argument_list|(
name|expectedArray
argument_list|)
expr_stmt|;
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|keySet
argument_list|()
argument_list|,
name|keys
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|()
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|WARMUP_SIZE
argument_list|,
name|keys
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|WARMUP_MIN
init|;
name|i
operator|<
name|WARMUP_MAX
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|key
init|=
name|warmed
operator|.
name|get
argument_list|(
name|i
operator|-
name|WARMUP_MIN
argument_list|)
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|keys
operator|.
name|contains
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|keys
operator|.
name|remove
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|keys
operator|.
name|remove
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|keys
operator|.
name|contains
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
DECL|method|testValues_populated ()
specifier|public
name|void
name|testValues_populated
parameter_list|()
block|{
for|for
control|(
name|Cache
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
name|List
argument_list|<
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|warmed
init|=
name|warmUp
argument_list|(
name|cache
argument_list|)
decl_stmt|;
name|Object
index|[]
name|expectedArray
init|=
name|Maps
operator|.
name|newHashMap
argument_list|(
name|cache
operator|.
name|asMap
argument_list|()
argument_list|)
operator|.
name|values
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|Object
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|values
argument_list|)
operator|.
name|hasContentsAnyOrder
argument_list|(
name|expectedArray
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|asList
argument_list|(
name|values
operator|.
name|toArray
argument_list|()
argument_list|)
argument_list|)
operator|.
name|hasContentsAnyOrder
argument_list|(
name|expectedArray
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|asList
argument_list|(
name|values
operator|.
name|toArray
argument_list|(
operator|new
name|Object
index|[
name|cache
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
argument_list|)
operator|.
name|hasContentsAnyOrder
argument_list|(
name|expectedArray
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|WARMUP_SIZE
argument_list|,
name|values
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|WARMUP_MIN
init|;
name|i
operator|<
name|WARMUP_MAX
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|value
init|=
name|warmed
operator|.
name|get
argument_list|(
name|i
operator|-
name|WARMUP_MIN
argument_list|)
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|values
operator|.
name|contains
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|values
operator|.
name|remove
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|values
operator|.
name|remove
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|values
operator|.
name|contains
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// generic array creation
DECL|method|testEntrySet_populated ()
specifier|public
name|void
name|testEntrySet_populated
parameter_list|()
block|{
for|for
control|(
name|Cache
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
name|List
argument_list|<
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|warmed
init|=
name|warmUp
argument_list|(
name|cache
argument_list|,
name|WARMUP_MIN
argument_list|,
name|WARMUP_MAX
argument_list|)
decl_stmt|;
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
name|Maps
operator|.
name|newHashMap
argument_list|(
name|cache
operator|.
name|asMap
argument_list|()
argument_list|)
operator|.
name|entrySet
argument_list|()
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|entries
argument_list|)
operator|.
name|is
argument_list|(
name|entrySet
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|entries
operator|.
name|toArray
argument_list|()
argument_list|)
operator|.
name|hasContentsAnyOrder
argument_list|(
name|entrySet
operator|.
name|toArray
argument_list|()
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|entries
operator|.
name|toArray
argument_list|(
operator|new
name|Entry
index|[
literal|0
index|]
argument_list|)
argument_list|)
operator|.
name|hasContentsAnyOrder
argument_list|(
name|entrySet
operator|.
name|toArray
argument_list|(
operator|new
name|Entry
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|,
name|entries
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|()
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|WARMUP_SIZE
argument_list|,
name|entries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|WARMUP_MIN
init|;
name|i
operator|<
name|WARMUP_MAX
condition|;
name|i
operator|++
control|)
block|{
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|newEntry
init|=
name|warmed
operator|.
name|get
argument_list|(
name|i
operator|-
name|WARMUP_MIN
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|entries
operator|.
name|contains
argument_list|(
name|newEntry
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|entries
operator|.
name|remove
argument_list|(
name|newEntry
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|entries
operator|.
name|remove
argument_list|(
name|newEntry
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|entries
operator|.
name|contains
argument_list|(
name|newEntry
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|checkEmpty
argument_list|(
name|entries
argument_list|)
expr_stmt|;
name|checkEmpty
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testWriteThroughEntry ()
specifier|public
name|void
name|testWriteThroughEntry
parameter_list|()
block|{
for|for
control|(
name|Cache
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
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|entry
operator|.
name|setValue
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expected entry.setValue to throw UnsupportedOperationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{       }
try|try
block|{
name|entry
operator|.
name|setValue
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expected entry.setValue(null) to throw UnsupportedOperationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{       }
name|checkValidState
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
name|Cache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|caches
parameter_list|()
block|{
comment|// lots of different ways to configure a Cache
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
name|Cache
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
name|Cache
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
comment|// This is trickier than expected. We plan to put 15 values in each of these (WARMUP_MIN to
comment|// WARMUP_MAX), but the tests assume no values get evicted. Even with a maximumSize of 100, one
comment|// of the values gets evicted. With weak/soft keys, we use identity equality, which means using
comment|// System.identityHashCode, which means the assignment of keys to segments is nondeterministic,
comment|// so more than (maximumSize / #segments) keys could get assigned to the same segment, which
comment|// would cause one to be evicted.
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
literal|400
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
literal|10
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
literal|10
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
DECL|method|warmUp (Cache<Object, Object> cache)
specifier|private
name|List
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|warmUp
parameter_list|(
name|Cache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
parameter_list|)
block|{
return|return
name|warmUp
argument_list|(
name|cache
argument_list|,
name|WARMUP_MIN
argument_list|,
name|WARMUP_MAX
argument_list|)
return|;
block|}
comment|/**    * Returns the entries that were added to the map, so they won't fall out of a map with weak or    * soft references until the caller drops the reference to the returned entries.    */
DECL|method|warmUp ( Cache<Object, Object> cache, int minimum, int maximum)
specifier|private
name|List
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|warmUp
parameter_list|(
name|Cache
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
name|List
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|entries
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
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
name|Object
name|key
init|=
name|i
decl_stmt|;
name|Object
name|value
init|=
name|cache
operator|.
name|getUnchecked
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|entries
operator|.
name|add
argument_list|(
name|entryOf
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|entries
return|;
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
DECL|method|assertMapSize (Map<?, ?> map, int size)
specifier|private
name|void
name|assertMapSize
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
parameter_list|,
name|int
name|size
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|size
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|size
operator|>
literal|0
condition|)
block|{
name|assertFalse
argument_list|(
name|map
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertTrue
argument_list|(
name|map
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertCollectionSize
argument_list|(
name|map
operator|.
name|keySet
argument_list|()
argument_list|,
name|size
argument_list|)
expr_stmt|;
name|assertCollectionSize
argument_list|(
name|map
operator|.
name|entrySet
argument_list|()
argument_list|,
name|size
argument_list|)
expr_stmt|;
name|assertCollectionSize
argument_list|(
name|map
operator|.
name|values
argument_list|()
argument_list|,
name|size
argument_list|)
expr_stmt|;
block|}
DECL|method|assertCollectionSize (Collection<?> collection, int size)
specifier|private
name|void
name|assertCollectionSize
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|,
name|int
name|size
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|size
argument_list|,
name|collection
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|size
operator|>
literal|0
condition|)
block|{
name|assertFalse
argument_list|(
name|collection
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertTrue
argument_list|(
name|collection
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|size
argument_list|,
name|Iterables
operator|.
name|size
argument_list|(
name|collection
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|size
argument_list|,
name|Iterators
operator|.
name|size
argument_list|(
name|collection
operator|.
name|iterator
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

