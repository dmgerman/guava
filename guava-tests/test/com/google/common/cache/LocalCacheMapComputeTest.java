begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2017 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|function
operator|.
name|IntConsumer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|IntStream
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

begin_comment
comment|/**  * Test Java8 map.compute in concurrent cache context.  */
end_comment

begin_class
DECL|class|LocalCacheMapComputeTest
specifier|public
class|class
name|LocalCacheMapComputeTest
extends|extends
name|TestCase
block|{
DECL|field|count
specifier|final
name|int
name|count
init|=
literal|10000
decl_stmt|;
DECL|field|delimiter
specifier|final
name|String
name|delimiter
init|=
literal|"-"
decl_stmt|;
DECL|field|key
specifier|final
name|String
name|key
init|=
literal|"key"
decl_stmt|;
DECL|field|cache
name|Cache
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|cache
decl_stmt|;
comment|// helper
DECL|method|doParallelCacheOp (int count, IntConsumer consumer)
specifier|private
specifier|static
name|void
name|doParallelCacheOp
parameter_list|(
name|int
name|count
parameter_list|,
name|IntConsumer
name|consumer
parameter_list|)
block|{
name|IntStream
operator|.
name|range
argument_list|(
literal|0
argument_list|,
name|count
argument_list|)
operator|.
name|parallel
argument_list|()
operator|.
name|forEach
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
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
name|this
operator|.
name|cache
operator|=
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|expireAfterAccess
argument_list|(
literal|500000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
operator|.
name|maximumSize
argument_list|(
name|count
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
DECL|method|testComputeIfAbsent ()
specifier|public
name|void
name|testComputeIfAbsent
parameter_list|()
block|{
comment|// simultaneous insertion for same key, expect 1 winner
name|doParallelCacheOp
argument_list|(
name|count
argument_list|,
name|n
lambda|->
block|{
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|computeIfAbsent
argument_list|(
name|key
argument_list|,
name|k
lambda|->
literal|"value"
operator|+
name|n
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testComputeIfPresent ()
specifier|public
name|void
name|testComputeIfPresent
parameter_list|()
block|{
name|cache
operator|.
name|put
argument_list|(
name|key
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
comment|// simultaneous update for same key, expect count successful updates
name|doParallelCacheOp
argument_list|(
name|count
argument_list|,
name|n
lambda|->
block|{
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|computeIfPresent
argument_list|(
name|key
argument_list|,
parameter_list|(
name|k
parameter_list|,
name|v
parameter_list|)
lambda|->
name|v
operator|+
name|delimiter
operator|+
name|n
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|cache
operator|.
name|getIfPresent
argument_list|(
name|key
argument_list|)
operator|.
name|split
argument_list|(
name|delimiter
argument_list|)
argument_list|)
operator|.
name|hasLength
argument_list|(
name|count
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|testUpdates ()
specifier|public
name|void
name|testUpdates
parameter_list|()
block|{
name|cache
operator|.
name|put
argument_list|(
name|key
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
comment|// simultaneous update for same key, some null, some non-null
name|doParallelCacheOp
argument_list|(
name|count
argument_list|,
name|n
lambda|->
block|{
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|compute
argument_list|(
name|key
argument_list|,
operator|(
name|k
operator|,
name|v
operator|)
operator|->
name|n
operator|%
literal|2
operator|==
literal|0
condition|?
name|v
operator|+
name|delimiter
operator|+
name|n
else|:
literal|null
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|1
operator|>=
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCompute ()
specifier|public
name|void
name|testCompute
parameter_list|()
block|{
name|cache
operator|.
name|put
argument_list|(
name|key
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
comment|// simultaneous deletion
name|doParallelCacheOp
argument_list|(
name|count
argument_list|,
name|n
lambda|->
block|{
name|cache
operator|.
name|asMap
argument_list|()
operator|.
name|compute
argument_list|(
name|key
argument_list|,
parameter_list|(
name|k
parameter_list|,
name|v
parameter_list|)
lambda|->
literal|null
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

