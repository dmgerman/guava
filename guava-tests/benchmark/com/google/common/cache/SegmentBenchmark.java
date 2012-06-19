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
name|base
operator|.
name|Preconditions
operator|.
name|checkState
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
name|Param
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
name|Runner
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
name|SimpleBenchmark
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
name|LocalCache
operator|.
name|Segment
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
name|AtomicReferenceArray
import|;
end_import

begin_comment
comment|/**  * Benchmark for {@code LocalCache.Segment.expand()}.  *  * @author Charles Fry  */
end_comment

begin_class
DECL|class|SegmentBenchmark
specifier|public
class|class
name|SegmentBenchmark
extends|extends
name|SimpleBenchmark
block|{
DECL|field|capacity
annotation|@
name|Param
argument_list|(
block|{
literal|"16"
block|,
literal|"32"
block|,
literal|"64"
block|,
literal|"128"
block|,
literal|"256"
block|,
literal|"512"
block|,
literal|"1024"
block|,
literal|"2048"
block|,
literal|"4096"
block|,
literal|"8192"
block|}
argument_list|)
name|int
name|capacity
decl_stmt|;
DECL|field|segment
specifier|private
name|Segment
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|segment
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
block|{
name|LocalCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
init|=
operator|new
name|LocalCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|(
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
name|initialCapacity
argument_list|(
name|capacity
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|checkState
argument_list|(
name|cache
operator|.
name|segments
operator|.
name|length
operator|==
literal|1
argument_list|)
expr_stmt|;
name|segment
operator|=
name|cache
operator|.
name|segments
index|[
literal|0
index|]
expr_stmt|;
name|checkState
argument_list|(
name|segment
operator|.
name|table
operator|.
name|length
argument_list|()
operator|==
name|capacity
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|segment
operator|.
name|threshold
condition|;
name|i
operator|++
control|)
block|{
name|cache
operator|.
name|put
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|,
operator|new
name|Object
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|checkState
argument_list|(
name|segment
operator|.
name|table
operator|.
name|length
argument_list|()
operator|==
name|capacity
argument_list|)
expr_stmt|;
block|}
DECL|method|time (int reps)
specifier|public
name|int
name|time
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|int
name|dummy
init|=
literal|0
decl_stmt|;
name|AtomicReferenceArray
argument_list|<
name|ReferenceEntry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|oldTable
init|=
name|segment
operator|.
name|table
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
name|reps
condition|;
name|i
operator|++
control|)
block|{
name|segment
operator|.
name|expand
argument_list|()
expr_stmt|;
name|segment
operator|.
name|table
operator|=
name|oldTable
expr_stmt|;
name|dummy
operator|+=
name|segment
operator|.
name|count
expr_stmt|;
block|}
return|return
name|dummy
return|;
block|}
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|Runner
operator|.
name|main
argument_list|(
name|SegmentBenchmark
operator|.
name|class
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

