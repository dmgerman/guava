begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|CacheBuilder
operator|.
name|EMPTY_STATS
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
name|LocalCacheTest
operator|.
name|SMALL_MAX_SIZE
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
name|cache
operator|.
name|LocalCache
operator|.
name|AutoLocalCache
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
name|Segment
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
name|NullPointerTester
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
name|util
operator|.
name|concurrent
operator|.
name|Callables
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
name|lang
operator|.
name|Thread
operator|.
name|UncaughtExceptionHandler
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
name|Callable
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
name|ConcurrentMap
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
name|CountDownLatch
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
name|TimeUnit
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
name|atomic
operator|.
name|AtomicReference
import|;
end_import

begin_comment
comment|/**  * @author Charles Fry  */
end_comment

begin_class
DECL|class|AutoLocalCacheTest
specifier|public
class|class
name|AutoLocalCacheTest
extends|extends
name|TestCase
block|{
DECL|method|makeCache ( CacheBuilder<K, V> builder, CacheLoader<? super K, V> loader)
specifier|private
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|AutoLocalCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|makeCache
parameter_list|(
name|CacheBuilder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|builder
parameter_list|,
name|CacheLoader
argument_list|<
name|?
super|super
name|K
argument_list|,
name|V
argument_list|>
name|loader
parameter_list|)
block|{
return|return
operator|new
name|AutoLocalCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|builder
argument_list|,
name|loader
argument_list|)
return|;
block|}
DECL|method|createCacheBuilder ()
specifier|private
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|createCacheBuilder
parameter_list|()
block|{
return|return
operator|new
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
return|;
block|}
comment|// constructor tests
DECL|method|testComputingFunction ()
specifier|public
name|void
name|testComputingFunction
parameter_list|()
block|{
name|CacheLoader
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|loader
init|=
operator|new
name|CacheLoader
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|load
parameter_list|(
name|Object
name|from
parameter_list|)
block|{
return|return
operator|new
name|Object
argument_list|()
return|;
block|}
block|}
decl_stmt|;
name|AutoLocalCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
init|=
name|makeCache
argument_list|(
name|createCacheBuilder
argument_list|()
argument_list|,
name|loader
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|loader
argument_list|,
name|cache
operator|.
name|loader
argument_list|)
expr_stmt|;
block|}
comment|// null parameters test
DECL|method|testNullParameters ()
specifier|public
name|void
name|testNullParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|NullPointerTester
name|tester
init|=
operator|new
name|NullPointerTester
argument_list|()
decl_stmt|;
name|tester
operator|.
name|setDefault
argument_list|(
name|Callable
operator|.
name|class
argument_list|,
name|Callables
operator|.
name|returning
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|CacheLoader
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|loader
init|=
name|identityLoader
argument_list|()
decl_stmt|;
name|tester
operator|.
name|testAllPublicInstanceMethods
argument_list|(
name|makeCache
argument_list|(
name|createCacheBuilder
argument_list|()
argument_list|,
name|loader
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// stats tests
DECL|method|testStats ()
specifier|public
name|void
name|testStats
parameter_list|()
block|{
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|builder
init|=
name|createCacheBuilder
argument_list|()
operator|.
name|concurrencyLevel
argument_list|(
literal|1
argument_list|)
operator|.
name|maximumSize
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|AutoLocalCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
init|=
name|makeCache
argument_list|(
name|builder
argument_list|,
name|identityLoader
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|EMPTY_STATS
argument_list|,
name|cache
operator|.
name|stats
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|one
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|cache
operator|.
name|getUnchecked
argument_list|(
name|one
argument_list|)
expr_stmt|;
name|CacheStats
name|stats
init|=
name|cache
operator|.
name|stats
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|stats
operator|.
name|requestCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|stats
operator|.
name|hitCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0.0
argument_list|,
name|stats
operator|.
name|hitRate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|stats
operator|.
name|missCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1.0
argument_list|,
name|stats
operator|.
name|missRate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|stats
operator|.
name|loadCount
argument_list|()
argument_list|)
expr_stmt|;
name|long
name|totalLoadTime
init|=
name|stats
operator|.
name|totalLoadTime
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|totalLoadTime
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|stats
operator|.
name|averageLoadPenalty
argument_list|()
operator|>
literal|0.0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|stats
operator|.
name|evictionCount
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|getUnchecked
argument_list|(
name|one
argument_list|)
expr_stmt|;
name|stats
operator|=
name|cache
operator|.
name|stats
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|stats
operator|.
name|requestCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|stats
operator|.
name|hitCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1.0
operator|/
literal|2
argument_list|,
name|stats
operator|.
name|hitRate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|stats
operator|.
name|missCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1.0
operator|/
literal|2
argument_list|,
name|stats
operator|.
name|missRate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|stats
operator|.
name|loadCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|stats
operator|.
name|evictionCount
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|two
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|cache
operator|.
name|getUnchecked
argument_list|(
name|two
argument_list|)
expr_stmt|;
name|stats
operator|=
name|cache
operator|.
name|stats
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|stats
operator|.
name|requestCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|stats
operator|.
name|hitCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1.0
operator|/
literal|3
argument_list|,
name|stats
operator|.
name|hitRate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|stats
operator|.
name|missCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2.0
operator|/
literal|3
argument_list|,
name|stats
operator|.
name|missRate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|stats
operator|.
name|loadCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|stats
operator|.
name|totalLoadTime
argument_list|()
operator|>
name|totalLoadTime
argument_list|)
expr_stmt|;
name|totalLoadTime
operator|=
name|stats
operator|.
name|totalLoadTime
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|stats
operator|.
name|averageLoadPenalty
argument_list|()
operator|>
literal|0.0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|stats
operator|.
name|evictionCount
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|three
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|cache
operator|.
name|getUnchecked
argument_list|(
name|three
argument_list|)
expr_stmt|;
name|stats
operator|=
name|cache
operator|.
name|stats
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|stats
operator|.
name|requestCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|stats
operator|.
name|hitCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1.0
operator|/
literal|4
argument_list|,
name|stats
operator|.
name|hitRate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|stats
operator|.
name|missCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3.0
operator|/
literal|4
argument_list|,
name|stats
operator|.
name|missRate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|stats
operator|.
name|loadCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|stats
operator|.
name|totalLoadTime
argument_list|()
operator|>
name|totalLoadTime
argument_list|)
expr_stmt|;
name|totalLoadTime
operator|=
name|stats
operator|.
name|totalLoadTime
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|stats
operator|.
name|averageLoadPenalty
argument_list|()
operator|>
literal|0.0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|stats
operator|.
name|evictionCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testStatsNoops ()
specifier|public
name|void
name|testStatsNoops
parameter_list|()
block|{
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|builder
init|=
name|createCacheBuilder
argument_list|()
operator|.
name|concurrencyLevel
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|AutoLocalCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
init|=
name|makeCache
argument_list|(
name|builder
argument_list|,
name|identityLoader
argument_list|()
argument_list|)
decl_stmt|;
name|ConcurrentMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|cache
operator|.
name|localCache
decl_stmt|;
comment|// modifiable map view
name|assertEquals
argument_list|(
name|EMPTY_STATS
argument_list|,
name|cache
operator|.
name|stats
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|one
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|put
argument_list|(
name|one
argument_list|,
name|one
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|one
argument_list|,
name|map
operator|.
name|get
argument_list|(
name|one
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|containsKey
argument_list|(
name|one
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|containsValue
argument_list|(
name|one
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|two
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|one
argument_list|,
name|map
operator|.
name|replace
argument_list|(
name|one
argument_list|,
name|two
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|containsKey
argument_list|(
name|one
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|containsValue
argument_list|(
name|one
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|three
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|replace
argument_list|(
name|one
argument_list|,
name|two
argument_list|,
name|three
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|remove
argument_list|(
name|one
argument_list|,
name|three
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|containsKey
argument_list|(
name|one
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|containsValue
argument_list|(
name|one
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|putIfAbsent
argument_list|(
name|two
argument_list|,
name|three
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|three
argument_list|,
name|map
operator|.
name|remove
argument_list|(
name|two
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|put
argument_list|(
name|three
argument_list|,
name|one
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|put
argument_list|(
name|one
argument_list|,
name|two
argument_list|)
argument_list|)
expr_stmt|;
name|Set
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
name|map
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
name|hasContentsAnyOrder
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|three
argument_list|,
name|one
argument_list|)
argument_list|,
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|one
argument_list|,
name|two
argument_list|)
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|keys
init|=
name|map
operator|.
name|keySet
argument_list|()
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
name|one
argument_list|,
name|three
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Object
argument_list|>
name|values
init|=
name|map
operator|.
name|values
argument_list|()
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
name|one
argument_list|,
name|two
argument_list|)
expr_stmt|;
name|map
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|EMPTY_STATS
argument_list|,
name|cache
operator|.
name|stats
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testDisableStats ()
specifier|public
name|void
name|testDisableStats
parameter_list|()
block|{
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|builder
init|=
name|createCacheBuilder
argument_list|()
operator|.
name|concurrencyLevel
argument_list|(
literal|1
argument_list|)
operator|.
name|maximumSize
argument_list|(
literal|2
argument_list|)
operator|.
name|disableStats
argument_list|()
decl_stmt|;
name|AutoLocalCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
init|=
name|makeCache
argument_list|(
name|builder
argument_list|,
name|identityLoader
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|EMPTY_STATS
argument_list|,
name|cache
operator|.
name|stats
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|one
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|cache
operator|.
name|getUnchecked
argument_list|(
name|one
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|EMPTY_STATS
argument_list|,
name|cache
operator|.
name|stats
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|getUnchecked
argument_list|(
name|one
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|EMPTY_STATS
argument_list|,
name|cache
operator|.
name|stats
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|two
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|cache
operator|.
name|getUnchecked
argument_list|(
name|two
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|EMPTY_STATS
argument_list|,
name|cache
operator|.
name|stats
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|three
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|cache
operator|.
name|getUnchecked
argument_list|(
name|three
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|EMPTY_STATS
argument_list|,
name|cache
operator|.
name|stats
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// asMap tests
DECL|method|testAsMap ()
specifier|public
name|void
name|testAsMap
parameter_list|()
block|{
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|builder
init|=
name|createCacheBuilder
argument_list|()
decl_stmt|;
name|AutoLocalCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
init|=
name|makeCache
argument_list|(
name|builder
argument_list|,
name|identityLoader
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|EMPTY_STATS
argument_list|,
name|cache
operator|.
name|stats
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|one
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Object
name|two
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Object
name|three
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|ConcurrentMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|cache
operator|.
name|asMap
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|put
argument_list|(
name|one
argument_list|,
name|two
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|two
argument_list|,
name|map
operator|.
name|get
argument_list|(
name|one
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|putAll
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
name|two
argument_list|,
name|three
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|three
argument_list|,
name|map
operator|.
name|get
argument_list|(
name|two
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|two
argument_list|,
name|map
operator|.
name|putIfAbsent
argument_list|(
name|one
argument_list|,
name|three
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|two
argument_list|,
name|map
operator|.
name|get
argument_list|(
name|one
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|putIfAbsent
argument_list|(
name|three
argument_list|,
name|one
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|one
argument_list|,
name|map
operator|.
name|get
argument_list|(
name|three
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|two
argument_list|,
name|map
operator|.
name|replace
argument_list|(
name|one
argument_list|,
name|three
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|three
argument_list|,
name|map
operator|.
name|get
argument_list|(
name|one
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|replace
argument_list|(
name|one
argument_list|,
name|two
argument_list|,
name|three
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|three
argument_list|,
name|map
operator|.
name|get
argument_list|(
name|one
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|replace
argument_list|(
name|one
argument_list|,
name|three
argument_list|,
name|two
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|two
argument_list|,
name|map
operator|.
name|get
argument_list|(
name|one
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|getUnchecked
argument_list|(
name|one
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|one
argument_list|,
name|map
operator|.
name|get
argument_list|(
name|one
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|containsKey
argument_list|(
name|one
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|containsValue
argument_list|(
name|one
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|one
argument_list|,
name|map
operator|.
name|remove
argument_list|(
name|one
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|getUnchecked
argument_list|(
name|one
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|remove
argument_list|(
name|one
argument_list|,
name|two
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|remove
argument_list|(
name|one
argument_list|,
name|one
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|getUnchecked
argument_list|(
name|one
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|newMap
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
name|one
argument_list|,
name|one
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|newMap
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|newMap
operator|.
name|entrySet
argument_list|()
argument_list|,
name|map
operator|.
name|entrySet
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|newMap
operator|.
name|keySet
argument_list|()
argument_list|,
name|map
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|expectedValues
init|=
name|ImmutableSet
operator|.
name|of
argument_list|(
name|one
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|actualValues
init|=
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|map
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedValues
argument_list|,
name|actualValues
argument_list|)
expr_stmt|;
block|}
comment|/**    * Lookups on the map view shouldn't impact the recency queue.    */
DECL|method|testAsMapRecency ()
specifier|public
name|void
name|testAsMapRecency
parameter_list|()
block|{
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|builder
init|=
name|createCacheBuilder
argument_list|()
operator|.
name|concurrencyLevel
argument_list|(
literal|1
argument_list|)
operator|.
name|maximumSize
argument_list|(
name|SMALL_MAX_SIZE
argument_list|)
decl_stmt|;
name|AutoLocalCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
init|=
name|makeCache
argument_list|(
name|builder
argument_list|,
name|identityLoader
argument_list|()
argument_list|)
decl_stmt|;
name|Segment
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|segment
init|=
name|cache
operator|.
name|localCache
operator|.
name|segments
index|[
literal|0
index|]
decl_stmt|;
name|ConcurrentMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|cache
operator|.
name|asMap
argument_list|()
decl_stmt|;
name|Object
name|one
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|one
argument_list|,
name|cache
operator|.
name|getUnchecked
argument_list|(
name|one
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|segment
operator|.
name|recencyQueue
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|one
argument_list|,
name|map
operator|.
name|get
argument_list|(
name|one
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|one
argument_list|,
name|segment
operator|.
name|recencyQueue
operator|.
name|peek
argument_list|()
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|one
argument_list|,
name|cache
operator|.
name|getUnchecked
argument_list|(
name|one
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|segment
operator|.
name|recencyQueue
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testRecursiveComputation ()
specifier|public
name|void
name|testRecursiveComputation
parameter_list|()
throws|throws
name|InterruptedException
block|{
specifier|final
name|AtomicReference
argument_list|<
name|Cache
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|>
name|cacheRef
init|=
operator|new
name|AtomicReference
argument_list|<
name|Cache
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|CacheLoader
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|recursiveLoader
init|=
operator|new
name|CacheLoader
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|load
parameter_list|(
name|Integer
name|key
parameter_list|)
block|{
if|if
condition|(
name|key
operator|>
literal|0
condition|)
block|{
return|return
name|key
operator|+
literal|", "
operator|+
name|cacheRef
operator|.
name|get
argument_list|()
operator|.
name|getUnchecked
argument_list|(
name|key
operator|-
literal|1
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|"0"
return|;
block|}
block|}
block|}
decl_stmt|;
name|Cache
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|recursiveCache
init|=
operator|new
name|CacheBuilder
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|()
operator|.
name|weakKeys
argument_list|()
operator|.
name|weakValues
argument_list|()
operator|.
name|build
argument_list|(
name|recursiveLoader
argument_list|)
decl_stmt|;
name|cacheRef
operator|.
name|set
argument_list|(
name|recursiveCache
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"3, 2, 1, 0"
argument_list|,
name|recursiveCache
operator|.
name|getUnchecked
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|recursiveLoader
operator|=
operator|new
name|CacheLoader
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|load
parameter_list|(
name|Integer
name|key
parameter_list|)
block|{
return|return
name|cacheRef
operator|.
name|get
argument_list|()
operator|.
name|getUnchecked
argument_list|(
name|key
argument_list|)
return|;
block|}
block|}
expr_stmt|;
name|recursiveCache
operator|=
operator|new
name|CacheBuilder
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|()
operator|.
name|weakKeys
argument_list|()
operator|.
name|weakValues
argument_list|()
operator|.
name|build
argument_list|(
name|recursiveLoader
argument_list|)
expr_stmt|;
name|cacheRef
operator|.
name|set
argument_list|(
name|recursiveCache
argument_list|)
expr_stmt|;
comment|// tells the test when the compution has completed
specifier|final
name|CountDownLatch
name|doneSignal
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Thread
name|thread
init|=
operator|new
name|Thread
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|cacheRef
operator|.
name|get
argument_list|()
operator|.
name|getUnchecked
argument_list|(
literal|3
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|doneSignal
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
name|thread
operator|.
name|setUncaughtExceptionHandler
argument_list|(
operator|new
name|UncaughtExceptionHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|uncaughtException
parameter_list|(
name|Thread
name|t
parameter_list|,
name|Throwable
name|e
parameter_list|)
block|{}
block|}
argument_list|)
expr_stmt|;
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
name|boolean
name|done
init|=
name|doneSignal
operator|.
name|await
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|done
condition|)
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|StackTraceElement
name|trace
range|:
name|thread
operator|.
name|getStackTrace
argument_list|()
control|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"\tat "
argument_list|)
operator|.
name|append
argument_list|(
name|trace
argument_list|)
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
name|fail
argument_list|(
name|builder
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

