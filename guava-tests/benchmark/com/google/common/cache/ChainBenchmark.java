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
name|common
operator|.
name|cache
operator|.
name|LocalCache
operator|.
name|Segment
import|;
end_import

begin_comment
comment|/**  * Benchmark for {@code LocalCache.Segment.removeEntryFromChain}.  *  * @author Charles Fry  */
end_comment

begin_class
DECL|class|ChainBenchmark
specifier|public
class|class
name|ChainBenchmark
block|{
annotation|@
name|Param
argument_list|(
block|{
literal|"1"
block|,
literal|"2"
block|,
literal|"3"
block|,
literal|"4"
block|,
literal|"5"
block|,
literal|"6"
block|}
argument_list|)
DECL|field|length
name|int
name|length
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
DECL|field|head
specifier|private
name|ReferenceEntry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|head
decl_stmt|;
DECL|field|chain
specifier|private
name|ReferenceEntry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|chain
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GuardedBy"
argument_list|)
annotation|@
name|BeforeExperiment
DECL|method|setUp ()
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
argument_list|<>
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
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|segment
operator|=
name|cache
operator|.
name|segments
index|[
literal|0
index|]
expr_stmt|;
name|chain
operator|=
literal|null
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
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|key
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
comment|// TODO(b/145386688): This access should be guarded by 'this.segment', which is not currently
comment|// held
name|chain
operator|=
name|segment
operator|.
name|newEntry
argument_list|(
name|key
argument_list|,
name|cache
operator|.
name|hash
argument_list|(
name|key
argument_list|)
argument_list|,
name|chain
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|==
literal|0
condition|)
block|{
name|head
operator|=
name|chain
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GuardedBy"
argument_list|)
annotation|@
name|Benchmark
DECL|method|time (int reps)
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
comment|// TODO(b/145386688): This access should be guarded by 'this.segment', which is not currently
comment|// held
name|segment
operator|.
name|removeEntryFromChain
argument_list|(
name|chain
argument_list|,
name|head
argument_list|)
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
block|}
end_class

end_unit

