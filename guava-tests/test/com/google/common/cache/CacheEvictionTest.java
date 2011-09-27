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
name|TestingCacheLoaders
operator|.
name|identityLoader
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
name|TestingRemovalListeners
operator|.
name|countingRemovalListener
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
name|TestingWeighers
operator|.
name|constantWeigher
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
name|TestingWeighers
operator|.
name|intKeyWeigher
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
name|CacheTesting
operator|.
name|Receiver
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
name|ReferenceEntry
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
name|TestingCacheLoaders
operator|.
name|IdentityLoader
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
name|TestingRemovalListeners
operator|.
name|CountingRemovalListener
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
name|List
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
comment|/**  * Tests relating to cache eviction: what does and doesn't count toward maximumSize, what happens  * when maximumSize is reached, etc.  *  * @author mike nonemacher  */
end_comment

begin_class
DECL|class|CacheEvictionTest
specifier|public
class|class
name|CacheEvictionTest
extends|extends
name|TestCase
block|{
DECL|field|MAX_SIZE
specifier|static
specifier|final
name|int
name|MAX_SIZE
init|=
literal|100
decl_stmt|;
DECL|method|testEviction_setMaxSegmentSize ()
specifier|public
name|void
name|testEviction_setMaxSegmentSize
parameter_list|()
block|{
name|IdentityLoader
argument_list|<
name|Object
argument_list|>
name|loader
init|=
name|identityLoader
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
literal|1000
condition|;
name|i
operator|++
control|)
block|{
name|Cache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
init|=
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|maximumSize
argument_list|(
name|i
argument_list|)
operator|.
name|build
argument_list|(
name|loader
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|i
argument_list|,
name|CacheTesting
operator|.
name|getTotalSegmentSize
argument_list|(
name|cache
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testEviction_setMaxSegmentWeight ()
specifier|public
name|void
name|testEviction_setMaxSegmentWeight
parameter_list|()
block|{
name|IdentityLoader
argument_list|<
name|Object
argument_list|>
name|loader
init|=
name|identityLoader
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
literal|1000
condition|;
name|i
operator|++
control|)
block|{
name|Cache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
init|=
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|maximumWeight
argument_list|(
name|i
argument_list|)
operator|.
name|weigher
argument_list|(
name|constantWeigher
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|build
argument_list|(
name|loader
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|i
argument_list|,
name|CacheTesting
operator|.
name|getTotalSegmentSize
argument_list|(
name|cache
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testEviction_maxSizeOneSegment ()
specifier|public
name|void
name|testEviction_maxSizeOneSegment
parameter_list|()
block|{
name|IdentityLoader
argument_list|<
name|Integer
argument_list|>
name|loader
init|=
name|identityLoader
argument_list|()
decl_stmt|;
name|Cache
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|cache
init|=
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|concurrencyLevel
argument_list|(
literal|1
argument_list|)
operator|.
name|maximumSize
argument_list|(
name|MAX_SIZE
argument_list|)
operator|.
name|build
argument_list|(
name|loader
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|2
operator|*
name|MAX_SIZE
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
name|assertEquals
argument_list|(
name|Math
operator|.
name|min
argument_list|(
name|i
operator|+
literal|1
argument_list|,
name|MAX_SIZE
argument_list|)
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|MAX_SIZE
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|CacheTesting
operator|.
name|checkValidState
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
DECL|method|testEviction_maxWeightOneSegment ()
specifier|public
name|void
name|testEviction_maxWeightOneSegment
parameter_list|()
block|{
name|IdentityLoader
argument_list|<
name|Integer
argument_list|>
name|loader
init|=
name|identityLoader
argument_list|()
decl_stmt|;
name|Cache
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|cache
init|=
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|concurrencyLevel
argument_list|(
literal|1
argument_list|)
operator|.
name|maximumWeight
argument_list|(
literal|2
operator|*
name|MAX_SIZE
argument_list|)
operator|.
name|weigher
argument_list|(
name|constantWeigher
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|build
argument_list|(
name|loader
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|2
operator|*
name|MAX_SIZE
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
name|assertEquals
argument_list|(
name|Math
operator|.
name|min
argument_list|(
name|i
operator|+
literal|1
argument_list|,
name|MAX_SIZE
argument_list|)
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|MAX_SIZE
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|CacheTesting
operator|.
name|checkValidState
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
DECL|method|testEviction_maxSize ()
specifier|public
name|void
name|testEviction_maxSize
parameter_list|()
block|{
name|CountingRemovalListener
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|removalListener
init|=
name|countingRemovalListener
argument_list|()
decl_stmt|;
name|IdentityLoader
argument_list|<
name|Integer
argument_list|>
name|loader
init|=
name|identityLoader
argument_list|()
decl_stmt|;
name|Cache
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|cache
init|=
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|maximumSize
argument_list|(
name|MAX_SIZE
argument_list|)
operator|.
name|removalListener
argument_list|(
name|removalListener
argument_list|)
operator|.
name|build
argument_list|(
name|loader
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|2
operator|*
name|MAX_SIZE
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
name|assertTrue
argument_list|(
name|cache
operator|.
name|size
argument_list|()
operator|<=
name|MAX_SIZE
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|MAX_SIZE
argument_list|,
name|CacheTesting
operator|.
name|evictionQueueSize
argument_list|(
name|cache
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MAX_SIZE
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|CacheTesting
operator|.
name|processPendingNotifications
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MAX_SIZE
argument_list|,
name|removalListener
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|CacheTesting
operator|.
name|checkValidState
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
DECL|method|testEviction_maxWeight ()
specifier|public
name|void
name|testEviction_maxWeight
parameter_list|()
block|{
name|CountingRemovalListener
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|removalListener
init|=
name|countingRemovalListener
argument_list|()
decl_stmt|;
name|IdentityLoader
argument_list|<
name|Integer
argument_list|>
name|loader
init|=
name|identityLoader
argument_list|()
decl_stmt|;
name|Cache
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|cache
init|=
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|maximumWeight
argument_list|(
literal|2
operator|*
name|MAX_SIZE
argument_list|)
operator|.
name|weigher
argument_list|(
name|constantWeigher
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|removalListener
argument_list|(
name|removalListener
argument_list|)
operator|.
name|build
argument_list|(
name|loader
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|2
operator|*
name|MAX_SIZE
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
name|assertTrue
argument_list|(
name|cache
operator|.
name|size
argument_list|()
operator|<=
name|MAX_SIZE
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|MAX_SIZE
argument_list|,
name|CacheTesting
operator|.
name|evictionQueueSize
argument_list|(
name|cache
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MAX_SIZE
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|CacheTesting
operator|.
name|processPendingNotifications
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MAX_SIZE
argument_list|,
name|removalListener
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|CacheTesting
operator|.
name|checkValidState
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
DECL|method|testUpdateRecency_onGet ()
specifier|public
name|void
name|testUpdateRecency_onGet
parameter_list|()
block|{
name|IdentityLoader
argument_list|<
name|Integer
argument_list|>
name|loader
init|=
name|identityLoader
argument_list|()
decl_stmt|;
specifier|final
name|Cache
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|cache
init|=
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|maximumSize
argument_list|(
name|MAX_SIZE
argument_list|)
operator|.
name|build
argument_list|(
name|loader
argument_list|)
decl_stmt|;
name|CacheTesting
operator|.
name|checkRecency
argument_list|(
name|cache
argument_list|,
name|MAX_SIZE
argument_list|,
operator|new
name|Receiver
argument_list|<
name|ReferenceEntry
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|accept
parameter_list|(
name|ReferenceEntry
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|entry
parameter_list|)
block|{
name|cache
operator|.
name|getUnchecked
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|testUpdateRecency_onInvalidate ()
specifier|public
name|void
name|testUpdateRecency_onInvalidate
parameter_list|()
block|{
name|IdentityLoader
argument_list|<
name|Integer
argument_list|>
name|loader
init|=
name|identityLoader
argument_list|()
decl_stmt|;
specifier|final
name|Cache
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|cache
init|=
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|maximumSize
argument_list|(
name|MAX_SIZE
argument_list|)
operator|.
name|concurrencyLevel
argument_list|(
literal|1
argument_list|)
operator|.
name|build
argument_list|(
name|loader
argument_list|)
decl_stmt|;
name|CacheTesting
operator|.
name|checkRecency
argument_list|(
name|cache
argument_list|,
name|MAX_SIZE
argument_list|,
operator|new
name|Receiver
argument_list|<
name|ReferenceEntry
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|accept
parameter_list|(
name|ReferenceEntry
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|entry
parameter_list|)
block|{
name|Integer
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|cache
operator|.
name|invalidate
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|testEviction_lru ()
specifier|public
name|void
name|testEviction_lru
parameter_list|()
block|{
comment|// test lru within a single segment
name|IdentityLoader
argument_list|<
name|Integer
argument_list|>
name|loader
init|=
name|identityLoader
argument_list|()
decl_stmt|;
name|Cache
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|cache
init|=
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|concurrencyLevel
argument_list|(
literal|1
argument_list|)
operator|.
name|maximumSize
argument_list|(
literal|10
argument_list|)
operator|.
name|build
argument_list|(
name|loader
argument_list|)
decl_stmt|;
name|CacheTesting
operator|.
name|warmUp
argument_list|(
name|cache
argument_list|,
literal|0
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Integer
argument_list|>
name|keySet
init|=
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|keySet
argument_list|()
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|keySet
argument_list|)
operator|.
name|hasContentsAnyOrder
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|4
argument_list|,
literal|5
argument_list|,
literal|6
argument_list|,
literal|7
argument_list|,
literal|8
argument_list|,
literal|9
argument_list|)
expr_stmt|;
comment|// re-order
name|getAll
argument_list|(
name|cache
argument_list|,
name|asList
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|keySet
argument_list|)
operator|.
name|hasContentsAnyOrder
argument_list|(
literal|3
argument_list|,
literal|4
argument_list|,
literal|5
argument_list|,
literal|6
argument_list|,
literal|7
argument_list|,
literal|8
argument_list|,
literal|9
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
comment|// evict 3, 4, 5
name|getAll
argument_list|(
name|cache
argument_list|,
name|asList
argument_list|(
literal|10
argument_list|,
literal|11
argument_list|,
literal|12
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|keySet
argument_list|)
operator|.
name|hasContentsAnyOrder
argument_list|(
literal|6
argument_list|,
literal|7
argument_list|,
literal|8
argument_list|,
literal|9
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|,
literal|10
argument_list|,
literal|11
argument_list|,
literal|12
argument_list|)
expr_stmt|;
comment|// re-order
name|getAll
argument_list|(
name|cache
argument_list|,
name|asList
argument_list|(
literal|6
argument_list|,
literal|7
argument_list|,
literal|8
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|keySet
argument_list|)
operator|.
name|hasContentsAnyOrder
argument_list|(
literal|9
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|,
literal|10
argument_list|,
literal|11
argument_list|,
literal|12
argument_list|,
literal|6
argument_list|,
literal|7
argument_list|,
literal|8
argument_list|)
expr_stmt|;
comment|// evict 9, 0, 1
name|getAll
argument_list|(
name|cache
argument_list|,
name|asList
argument_list|(
literal|13
argument_list|,
literal|14
argument_list|,
literal|15
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|keySet
argument_list|)
operator|.
name|hasContentsAnyOrder
argument_list|(
literal|2
argument_list|,
literal|10
argument_list|,
literal|11
argument_list|,
literal|12
argument_list|,
literal|6
argument_list|,
literal|7
argument_list|,
literal|8
argument_list|,
literal|13
argument_list|,
literal|14
argument_list|,
literal|15
argument_list|)
expr_stmt|;
block|}
DECL|method|testEviction_weightedLru ()
specifier|public
name|void
name|testEviction_weightedLru
parameter_list|()
block|{
comment|// test weighted lru within a single segment
name|IdentityLoader
argument_list|<
name|Integer
argument_list|>
name|loader
init|=
name|identityLoader
argument_list|()
decl_stmt|;
name|Cache
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|cache
init|=
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|concurrencyLevel
argument_list|(
literal|1
argument_list|)
operator|.
name|maximumWeight
argument_list|(
literal|45
argument_list|)
operator|.
name|weigher
argument_list|(
name|intKeyWeigher
argument_list|()
argument_list|)
operator|.
name|build
argument_list|(
name|loader
argument_list|)
decl_stmt|;
name|CacheTesting
operator|.
name|warmUp
argument_list|(
name|cache
argument_list|,
literal|0
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Integer
argument_list|>
name|keySet
init|=
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|keySet
argument_list|()
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|keySet
argument_list|)
operator|.
name|hasContentsAnyOrder
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|4
argument_list|,
literal|5
argument_list|,
literal|6
argument_list|,
literal|7
argument_list|,
literal|8
argument_list|,
literal|9
argument_list|)
expr_stmt|;
comment|// re-order
name|getAll
argument_list|(
name|cache
argument_list|,
name|asList
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|keySet
argument_list|)
operator|.
name|hasContentsAnyOrder
argument_list|(
literal|3
argument_list|,
literal|4
argument_list|,
literal|5
argument_list|,
literal|6
argument_list|,
literal|7
argument_list|,
literal|8
argument_list|,
literal|9
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
comment|// evict 3, 4, 5
name|getAll
argument_list|(
name|cache
argument_list|,
name|asList
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|keySet
argument_list|)
operator|.
name|hasContentsAnyOrder
argument_list|(
literal|6
argument_list|,
literal|7
argument_list|,
literal|8
argument_list|,
literal|9
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|,
literal|10
argument_list|)
expr_stmt|;
comment|// re-order
name|getAll
argument_list|(
name|cache
argument_list|,
name|asList
argument_list|(
literal|6
argument_list|,
literal|7
argument_list|,
literal|8
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|keySet
argument_list|)
operator|.
name|hasContentsAnyOrder
argument_list|(
literal|9
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|,
literal|10
argument_list|,
literal|6
argument_list|,
literal|7
argument_list|,
literal|8
argument_list|)
expr_stmt|;
comment|// evict 9, 1, 2, 10
name|getAll
argument_list|(
name|cache
argument_list|,
name|asList
argument_list|(
literal|15
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|keySet
argument_list|)
operator|.
name|hasContentsAnyOrder
argument_list|(
literal|0
argument_list|,
literal|6
argument_list|,
literal|7
argument_list|,
literal|8
argument_list|,
literal|15
argument_list|)
expr_stmt|;
comment|// fill empty space
name|getAll
argument_list|(
name|cache
argument_list|,
name|asList
argument_list|(
literal|9
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|keySet
argument_list|)
operator|.
name|hasContentsAnyOrder
argument_list|(
literal|0
argument_list|,
literal|6
argument_list|,
literal|7
argument_list|,
literal|8
argument_list|,
literal|15
argument_list|,
literal|9
argument_list|)
expr_stmt|;
comment|// evict 6
name|getAll
argument_list|(
name|cache
argument_list|,
name|asList
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|keySet
argument_list|)
operator|.
name|hasContentsAnyOrder
argument_list|(
literal|0
argument_list|,
literal|7
argument_list|,
literal|8
argument_list|,
literal|15
argument_list|,
literal|9
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|testEviction_overweight ()
specifier|public
name|void
name|testEviction_overweight
parameter_list|()
block|{
comment|// test weighted lru within a single segment
name|IdentityLoader
argument_list|<
name|Integer
argument_list|>
name|loader
init|=
name|identityLoader
argument_list|()
decl_stmt|;
name|Cache
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|cache
init|=
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|concurrencyLevel
argument_list|(
literal|1
argument_list|)
operator|.
name|maximumWeight
argument_list|(
literal|45
argument_list|)
operator|.
name|weigher
argument_list|(
name|intKeyWeigher
argument_list|()
argument_list|)
operator|.
name|build
argument_list|(
name|loader
argument_list|)
decl_stmt|;
name|CacheTesting
operator|.
name|warmUp
argument_list|(
name|cache
argument_list|,
literal|0
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Integer
argument_list|>
name|keySet
init|=
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|keySet
argument_list|()
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|keySet
argument_list|)
operator|.
name|hasContentsAnyOrder
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|4
argument_list|,
literal|5
argument_list|,
literal|6
argument_list|,
literal|7
argument_list|,
literal|8
argument_list|,
literal|9
argument_list|)
expr_stmt|;
comment|// add an at-the-maximum-weight entry
name|getAll
argument_list|(
name|cache
argument_list|,
name|asList
argument_list|(
literal|45
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|keySet
argument_list|)
operator|.
name|hasContentsAnyOrder
argument_list|(
literal|0
argument_list|,
literal|45
argument_list|)
expr_stmt|;
comment|// add an over-the-maximum-weight entry
name|getAll
argument_list|(
name|cache
argument_list|,
name|asList
argument_list|(
literal|46
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|keySet
argument_list|)
operator|.
name|hasContentsAnyOrder
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|getAll (Cache<Integer, Integer> cache, List<Integer> keys)
specifier|private
name|void
name|getAll
parameter_list|(
name|Cache
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|cache
parameter_list|,
name|List
argument_list|<
name|Integer
argument_list|>
name|keys
parameter_list|)
block|{
for|for
control|(
name|int
name|i
range|:
name|keys
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
name|CacheTesting
operator|.
name|drainRecencyQueues
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

