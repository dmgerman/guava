begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
import|import
name|com
operator|.
name|google
operator|.
name|caliper
operator|.
name|BeforeExperiment
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|caliper
operator|.
name|Benchmark
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
name|MapMaker
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

begin_comment
comment|/**  * Compare CacheBuilder and MapMaker performance, ensuring that they remain on par with each other.  *  * @author Nikita Sidorov  */
end_comment

begin_class
DECL|class|MapMakerComparisonBenchmark
specifier|public
class|class
name|MapMakerComparisonBenchmark
block|{
DECL|field|TEST_KEY
specifier|private
specifier|static
specifier|final
name|String
name|TEST_KEY
init|=
literal|"test key"
decl_stmt|;
DECL|field|TEST_VALUE
specifier|private
specifier|static
specifier|final
name|String
name|TEST_VALUE
init|=
literal|"test value"
decl_stmt|;
comment|// Non-loading versions:
DECL|field|map
specifier|private
specifier|final
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|MapMaker
argument_list|()
operator|.
name|makeMap
argument_list|()
decl_stmt|;
comment|// Returns ConcurrentHashMap
DECL|field|cache
specifier|private
specifier|final
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
name|recordStats
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
DECL|field|cacheNoStats
specifier|private
specifier|final
name|Cache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cacheNoStats
init|=
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
annotation|@
name|BeforeExperiment
DECL|method|setUp ()
name|void
name|setUp
parameter_list|()
block|{
name|map
operator|.
name|put
argument_list|(
name|TEST_KEY
argument_list|,
name|TEST_VALUE
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|TEST_KEY
argument_list|,
name|TEST_VALUE
argument_list|)
expr_stmt|;
name|cacheNoStats
operator|.
name|put
argument_list|(
name|TEST_KEY
argument_list|,
name|TEST_VALUE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Benchmark
DECL|method|concurrentHashMap (int rep)
name|void
name|concurrentHashMap
parameter_list|(
name|int
name|rep
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|rep
condition|;
name|i
operator|++
control|)
block|{
name|map
operator|.
name|get
argument_list|(
name|TEST_KEY
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Benchmark
DECL|method|cacheBuilder_stats (int rep)
name|void
name|cacheBuilder_stats
parameter_list|(
name|int
name|rep
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|rep
condition|;
name|i
operator|++
control|)
block|{
name|cache
operator|.
name|getIfPresent
argument_list|(
name|TEST_KEY
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Benchmark
DECL|method|cacheBuilder (int rep)
name|void
name|cacheBuilder
parameter_list|(
name|int
name|rep
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|rep
condition|;
name|i
operator|++
control|)
block|{
name|cacheNoStats
operator|.
name|getIfPresent
argument_list|(
name|TEST_KEY
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

