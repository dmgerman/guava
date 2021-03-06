begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2014 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.util.concurrent
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
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
name|Iterables
operator|.
name|cycle
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
name|Iterables
operator|.
name|limit
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
name|caliper
operator|.
name|api
operator|.
name|Footprint
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
name|api
operator|.
name|VmOptions
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
name|Supplier
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
name|ImmutableList
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
name|primitives
operator|.
name|Ints
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
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
name|locks
operator|.
name|Lock
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
name|locks
operator|.
name|ReentrantLock
import|;
end_import

begin_comment
comment|/** A benchmark comparing the various striped implementations. */
end_comment

begin_class
annotation|@
name|VmOptions
argument_list|(
block|{
literal|"-Xms12g"
block|,
literal|"-Xmx12g"
block|,
literal|"-d64"
block|}
argument_list|)
DECL|class|StripedBenchmark
specifier|public
class|class
name|StripedBenchmark
block|{
DECL|field|LOCK_SUPPLIER
specifier|private
specifier|static
specifier|final
name|Supplier
argument_list|<
name|Lock
argument_list|>
name|LOCK_SUPPLIER
init|=
operator|new
name|Supplier
argument_list|<
name|Lock
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Lock
name|get
parameter_list|()
block|{
return|return
operator|new
name|ReentrantLock
argument_list|()
return|;
block|}
block|}
decl_stmt|;
annotation|@
name|Param
argument_list|(
block|{
literal|"2"
block|,
literal|"8"
block|,
literal|"64"
block|,
literal|"1024"
block|,
literal|"65536"
block|}
argument_list|)
DECL|field|numStripes
name|int
name|numStripes
decl_stmt|;
DECL|field|impl
annotation|@
name|Param
name|Impl
name|impl
decl_stmt|;
DECL|enum|Impl
enum|enum
name|Impl
block|{
DECL|enumConstant|EAGER
name|EAGER
block|{
annotation|@
name|Override
name|Striped
argument_list|<
name|Lock
argument_list|>
name|get
parameter_list|(
name|int
name|stripes
parameter_list|)
block|{
return|return
name|Striped
operator|.
name|lock
argument_list|(
name|stripes
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|LAZY_SMALL
name|LAZY_SMALL
block|{
annotation|@
name|Override
name|Striped
argument_list|<
name|Lock
argument_list|>
name|get
parameter_list|(
name|int
name|stripes
parameter_list|)
block|{
return|return
operator|new
name|Striped
operator|.
name|SmallLazyStriped
argument_list|<>
argument_list|(
name|stripes
argument_list|,
name|LOCK_SUPPLIER
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|LAZY_LARGE
name|LAZY_LARGE
block|{
annotation|@
name|Override
name|Striped
argument_list|<
name|Lock
argument_list|>
name|get
parameter_list|(
name|int
name|stripes
parameter_list|)
block|{
return|return
operator|new
name|Striped
operator|.
name|LargeLazyStriped
argument_list|<>
argument_list|(
name|stripes
argument_list|,
name|LOCK_SUPPLIER
argument_list|)
return|;
block|}
block|}
block|;
DECL|method|get (int stripes)
specifier|abstract
name|Striped
argument_list|<
name|Lock
argument_list|>
name|get
parameter_list|(
name|int
name|stripes
parameter_list|)
function_decl|;
block|}
DECL|field|striped
specifier|private
name|Striped
argument_list|<
name|Lock
argument_list|>
name|striped
decl_stmt|;
DECL|field|stripes
specifier|private
name|int
index|[]
name|stripes
decl_stmt|;
DECL|field|bulkGetSet
specifier|private
name|List
argument_list|<
name|Integer
argument_list|>
name|bulkGetSet
decl_stmt|;
annotation|@
name|BeforeExperiment
DECL|method|setUp ()
name|void
name|setUp
parameter_list|()
block|{
name|this
operator|.
name|striped
operator|=
name|impl
operator|.
name|get
argument_list|(
name|numStripes
argument_list|)
expr_stmt|;
name|stripes
operator|=
operator|new
name|int
index|[
name|numStripes
index|]
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
name|numStripes
condition|;
name|i
operator|++
control|)
block|{
name|stripes
index|[
name|i
index|]
operator|=
name|i
expr_stmt|;
block|}
name|List
argument_list|<
name|Integer
argument_list|>
name|asList
init|=
name|Ints
operator|.
name|asList
argument_list|(
name|stripes
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|shuffle
argument_list|(
name|asList
argument_list|,
operator|new
name|Random
argument_list|(
literal|0xdeadbeef
argument_list|)
argument_list|)
expr_stmt|;
comment|// do bulk gets with exactly 10 keys (possibly<10 stripes) (or less if numStripes is smaller)
name|bulkGetSet
operator|=
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|limit
argument_list|(
name|cycle
argument_list|(
name|asList
argument_list|)
argument_list|,
literal|10
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Footprint
DECL|method|sizeOfStriped ()
name|Object
name|sizeOfStriped
parameter_list|()
block|{
return|return
name|impl
operator|.
name|get
argument_list|(
name|numStripes
argument_list|)
return|;
block|}
comment|// a place to put the locks in sizeOfPopulatedStriped so they don't get GC'd before we measure
DECL|field|locks
specifier|final
name|List
argument_list|<
name|Lock
argument_list|>
name|locks
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|numStripes
argument_list|)
decl_stmt|;
annotation|@
name|Footprint
DECL|method|sizeOfPopulatedStriped ()
name|Object
name|sizeOfPopulatedStriped
parameter_list|()
block|{
name|locks
operator|.
name|clear
argument_list|()
expr_stmt|;
name|Striped
argument_list|<
name|Lock
argument_list|>
name|striped
init|=
name|impl
operator|.
name|get
argument_list|(
name|numStripes
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
range|:
name|stripes
control|)
block|{
name|locks
operator|.
name|add
argument_list|(
name|striped
operator|.
name|getAt
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|striped
return|;
block|}
annotation|@
name|Benchmark
DECL|method|timeConstruct (long reps)
name|long
name|timeConstruct
parameter_list|(
name|long
name|reps
parameter_list|)
block|{
name|long
name|rvalue
init|=
literal|0
decl_stmt|;
name|int
name|numStripesLocal
init|=
name|numStripes
decl_stmt|;
name|Impl
name|implLocal
init|=
name|impl
decl_stmt|;
for|for
control|(
name|long
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
name|rvalue
operator|+=
name|implLocal
operator|.
name|get
argument_list|(
name|numStripesLocal
argument_list|)
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
return|return
name|rvalue
return|;
block|}
annotation|@
name|Benchmark
DECL|method|timeGetAt (long reps)
name|long
name|timeGetAt
parameter_list|(
name|long
name|reps
parameter_list|)
block|{
name|long
name|rvalue
init|=
literal|0
decl_stmt|;
name|int
index|[]
name|stripesLocal
init|=
name|stripes
decl_stmt|;
name|int
name|mask
init|=
name|numStripes
operator|-
literal|1
decl_stmt|;
name|Striped
argument_list|<
name|Lock
argument_list|>
name|stripedLocal
init|=
name|striped
decl_stmt|;
for|for
control|(
name|long
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
name|rvalue
operator|+=
name|stripedLocal
operator|.
name|getAt
argument_list|(
name|stripesLocal
index|[
call|(
name|int
call|)
argument_list|(
name|i
operator|&
name|mask
argument_list|)
index|]
argument_list|)
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
return|return
name|rvalue
return|;
block|}
annotation|@
name|Benchmark
DECL|method|timeBulkGet (long reps)
name|long
name|timeBulkGet
parameter_list|(
name|long
name|reps
parameter_list|)
block|{
name|long
name|rvalue
init|=
literal|0
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|bulkGetSetLocal
init|=
name|bulkGetSet
decl_stmt|;
name|Striped
argument_list|<
name|Lock
argument_list|>
name|stripedLocal
init|=
name|striped
decl_stmt|;
for|for
control|(
name|long
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
name|rvalue
operator|+=
name|stripedLocal
operator|.
name|bulkGet
argument_list|(
name|bulkGetSetLocal
argument_list|)
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
return|return
name|rvalue
return|;
block|}
block|}
end_class

end_unit

