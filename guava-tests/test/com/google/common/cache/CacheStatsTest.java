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
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Unit test for {@link CacheStats}.  *  * @author Charles Fry  */
end_comment

begin_class
DECL|class|CacheStatsTest
specifier|public
class|class
name|CacheStatsTest
extends|extends
name|TestCase
block|{
DECL|method|testEmpty ()
specifier|public
name|void
name|testEmpty
parameter_list|()
block|{
name|CacheStats
name|stats
init|=
operator|new
name|CacheStats
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
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
literal|1.0
argument_list|,
name|stats
operator|.
name|hitRate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|stats
operator|.
name|missCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0.0
argument_list|,
name|stats
operator|.
name|missRate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|stats
operator|.
name|loadSuccessCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|stats
operator|.
name|loadExceptionCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0.0
argument_list|,
name|stats
operator|.
name|loadExceptionRate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
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
name|totalLoadTime
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0.0
argument_list|,
name|stats
operator|.
name|averageLoadPenalty
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
block|}
DECL|method|testSingle ()
specifier|public
name|void
name|testSingle
parameter_list|()
block|{
name|CacheStats
name|stats
init|=
operator|new
name|CacheStats
argument_list|(
literal|11
argument_list|,
literal|13
argument_list|,
literal|17
argument_list|,
literal|19
argument_list|,
literal|23
argument_list|,
literal|27
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|24
argument_list|,
name|stats
operator|.
name|requestCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|11
argument_list|,
name|stats
operator|.
name|hitCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|11.0
operator|/
literal|24
argument_list|,
name|stats
operator|.
name|hitRate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|13
argument_list|,
name|stats
operator|.
name|missCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|13.0
operator|/
literal|24
argument_list|,
name|stats
operator|.
name|missRate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|17
argument_list|,
name|stats
operator|.
name|loadSuccessCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|19
argument_list|,
name|stats
operator|.
name|loadExceptionCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|19.0
operator|/
literal|36
argument_list|,
name|stats
operator|.
name|loadExceptionRate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|17
operator|+
literal|19
argument_list|,
name|stats
operator|.
name|loadCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|23
argument_list|,
name|stats
operator|.
name|totalLoadTime
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|23.0
operator|/
operator|(
literal|17
operator|+
literal|19
operator|)
argument_list|,
name|stats
operator|.
name|averageLoadPenalty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|27
argument_list|,
name|stats
operator|.
name|evictionCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testMinus ()
specifier|public
name|void
name|testMinus
parameter_list|()
block|{
name|CacheStats
name|one
init|=
operator|new
name|CacheStats
argument_list|(
literal|11
argument_list|,
literal|13
argument_list|,
literal|17
argument_list|,
literal|19
argument_list|,
literal|23
argument_list|,
literal|27
argument_list|)
decl_stmt|;
name|CacheStats
name|two
init|=
operator|new
name|CacheStats
argument_list|(
literal|53
argument_list|,
literal|47
argument_list|,
literal|43
argument_list|,
literal|41
argument_list|,
literal|37
argument_list|,
literal|31
argument_list|)
decl_stmt|;
name|CacheStats
name|diff
init|=
name|two
operator|.
name|minus
argument_list|(
name|one
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|76
argument_list|,
name|diff
operator|.
name|requestCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|42
argument_list|,
name|diff
operator|.
name|hitCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|42.0
operator|/
literal|76
argument_list|,
name|diff
operator|.
name|hitRate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|34
argument_list|,
name|diff
operator|.
name|missCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|34.0
operator|/
literal|76
argument_list|,
name|diff
operator|.
name|missRate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|26
argument_list|,
name|diff
operator|.
name|loadSuccessCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|22
argument_list|,
name|diff
operator|.
name|loadExceptionCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|22.0
operator|/
literal|48
argument_list|,
name|diff
operator|.
name|loadExceptionRate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|26
operator|+
literal|22
argument_list|,
name|diff
operator|.
name|loadCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|14
argument_list|,
name|diff
operator|.
name|totalLoadTime
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|14.0
operator|/
operator|(
literal|26
operator|+
literal|22
operator|)
argument_list|,
name|diff
operator|.
name|averageLoadPenalty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|diff
operator|.
name|evictionCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|CacheStats
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
argument_list|,
name|one
operator|.
name|minus
argument_list|(
name|two
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testPlus ()
specifier|public
name|void
name|testPlus
parameter_list|()
block|{
name|CacheStats
name|one
init|=
operator|new
name|CacheStats
argument_list|(
literal|11
argument_list|,
literal|13
argument_list|,
literal|15
argument_list|,
literal|13
argument_list|,
literal|11
argument_list|,
literal|9
argument_list|)
decl_stmt|;
name|CacheStats
name|two
init|=
operator|new
name|CacheStats
argument_list|(
literal|53
argument_list|,
literal|47
argument_list|,
literal|41
argument_list|,
literal|39
argument_list|,
literal|37
argument_list|,
literal|35
argument_list|)
decl_stmt|;
name|CacheStats
name|sum
init|=
name|two
operator|.
name|plus
argument_list|(
name|one
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|124
argument_list|,
name|sum
operator|.
name|requestCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|64
argument_list|,
name|sum
operator|.
name|hitCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|64.0
operator|/
literal|124
argument_list|,
name|sum
operator|.
name|hitRate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|60
argument_list|,
name|sum
operator|.
name|missCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|60.0
operator|/
literal|124
argument_list|,
name|sum
operator|.
name|missRate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|56
argument_list|,
name|sum
operator|.
name|loadSuccessCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|52
argument_list|,
name|sum
operator|.
name|loadExceptionCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|52.0
operator|/
literal|108
argument_list|,
name|sum
operator|.
name|loadExceptionRate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|56
operator|+
literal|52
argument_list|,
name|sum
operator|.
name|loadCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|48
argument_list|,
name|sum
operator|.
name|totalLoadTime
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|48.0
operator|/
operator|(
literal|56
operator|+
literal|52
operator|)
argument_list|,
name|sum
operator|.
name|averageLoadPenalty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|44
argument_list|,
name|sum
operator|.
name|evictionCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|sum
argument_list|,
name|one
operator|.
name|plus
argument_list|(
name|two
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testPlusLarge ()
specifier|public
name|void
name|testPlusLarge
parameter_list|()
block|{
name|CacheStats
name|maxCacheStats
init|=
operator|new
name|CacheStats
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|,
name|Long
operator|.
name|MAX_VALUE
argument_list|,
name|Long
operator|.
name|MAX_VALUE
argument_list|,
name|Long
operator|.
name|MAX_VALUE
argument_list|,
name|Long
operator|.
name|MAX_VALUE
argument_list|,
name|Long
operator|.
name|MAX_VALUE
argument_list|)
decl_stmt|;
name|CacheStats
name|smallCacheStats
init|=
operator|new
name|CacheStats
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|CacheStats
name|sum
init|=
name|smallCacheStats
operator|.
name|plus
argument_list|(
name|maxCacheStats
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|,
name|sum
operator|.
name|requestCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|,
name|sum
operator|.
name|hitCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1.0
argument_list|,
name|sum
operator|.
name|hitRate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|,
name|sum
operator|.
name|missCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1.0
argument_list|,
name|sum
operator|.
name|missRate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|,
name|sum
operator|.
name|loadSuccessCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|,
name|sum
operator|.
name|loadExceptionCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1.0
argument_list|,
name|sum
operator|.
name|loadExceptionRate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|,
name|sum
operator|.
name|loadCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|,
name|sum
operator|.
name|totalLoadTime
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1.0
argument_list|,
name|sum
operator|.
name|averageLoadPenalty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|,
name|sum
operator|.
name|evictionCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|sum
argument_list|,
name|maxCacheStats
operator|.
name|plus
argument_list|(
name|smallCacheStats
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

